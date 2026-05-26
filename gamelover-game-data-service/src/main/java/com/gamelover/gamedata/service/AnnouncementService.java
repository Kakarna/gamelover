package com.gamelover.gamedata.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamelover.gamedata.dto.WuwaNoticeResponse;
import com.gamelover.gamedata.entity.Announcement;
import com.gamelover.gamedata.repository.AnnouncementRepository;
import com.gamelover.gamedata.vo.AnnouncementVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final MongoTemplate mongoTemplate;
    private final StringRedisTemplate redisTemplate;

    private static final String WUWA_NOTICE_URL = "https://aki-gm-resources-back.aki-game.com/gamenotice/G152/76402e5b20be2c39f095a152090afddc/zh-Hans.json";
    private static final String GAME_CODE_WUWA = "WUWA";
    private static final String CACHE_KEY_PREFIX = "announcement:";
    private static final long CACHE_EXPIRE_MINUTES = 5;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void syncWuwaAnnouncements() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(WUWA_NOTICE_URL, String.class);

            log.info("获取鸣潮公告成功, 响应长度: {}", response.length());

            WuwaNoticeResponse noticeResponse = parseNoticeResponse(response);
            if (noticeResponse != null && noticeResponse.getGame() != null) {
                processAnnouncements(noticeResponse.getGame());
                clearAnnouncementCache(GAME_CODE_WUWA);
            }
        } catch (Exception e) {
            log.error("同步鸣潮公告失败", e);
        }
    }

    private WuwaNoticeResponse parseNoticeResponse(String response) {
        try {
            return objectMapper.readValue(response, WuwaNoticeResponse.class);
        } catch (Exception e) {
            log.error("解析公告JSON失败", e);
            return null;
        }
    }

    private void processAnnouncements(List<WuwaNoticeResponse.GameNotice> gameNotices) {
        int savedCount = 0;
        int updatedCount = 0;

        for (WuwaNoticeResponse.GameNotice notice : gameNotices) {
            try {
                Announcement existing = announcementRepository.findByAnnouncementId(notice.getId()).orElse(null);

                if (existing == null) {
                    Announcement announcement = convertToEntity(notice);
                    announcementRepository.save(announcement);
                    savedCount++;
                } else {
                    updateExistingEntity(existing, notice);
                    announcementRepository.save(existing);
                    updatedCount++;
                }
            } catch (Exception e) {
                log.error("处理公告失败: {}", notice.getId(), e);
            }
        }

        log.info("鸣潮公告同步完成: 新增={}, 更新={}", savedCount, updatedCount);
    }

    private Announcement convertToEntity(WuwaNoticeResponse.GameNotice notice) {
        Announcement announcement = new Announcement();
        announcement.setAnnouncementId(notice.getId());
        announcement.setGameCode(GAME_CODE_WUWA);
        announcement.setContentHtml(notice.getContent());
        announcement.setTabBanner(notice.getTabBanner());
        announcement.setFoldBanner(notice.getFoldBanner());
        announcement.setIsRedDot(notice.getRed());
        announcement.setStartTimeMs(notice.getStartTimeMs());
        announcement.setEndTimeMs(notice.getEndTimeMs());
        announcement.setAnnouncementType(determineAnnouncementType(notice));
        announcement.setPriority(calculatePriority(notice));
        announcement.setCreateTime(Instant.now());
        announcement.setUpdateTime(Instant.now());

        String title = getNoticeTitle(notice);
        announcement.setTitle(title);

        return announcement;
    }

    private void updateExistingEntity(Announcement existing, WuwaNoticeResponse.GameNotice notice) {
        existing.setContentHtml(notice.getContent());
        existing.setTabBanner(notice.getTabBanner());
        existing.setFoldBanner(notice.getFoldBanner());
        existing.setIsRedDot(notice.getRed());
        existing.setStartTimeMs(notice.getStartTimeMs());
        existing.setEndTimeMs(notice.getEndTimeMs());
        existing.setAnnouncementType(determineAnnouncementType(notice));
        existing.setPriority(calculatePriority(notice));
        existing.setUpdateTime(Instant.now());

        String title = getNoticeTitle(notice);
        existing.setTitle(title);
    }

    private String determineAnnouncementType(WuwaNoticeResponse.GameNotice notice) {
        String content = notice.getContent().toLowerCase();
        if (content.contains("版本") || content.contains("维护") || content.contains("更新")) {
            return "VERSION";
        } else if (content.contains("活动") || content.contains("任务")) {
            return "EVENT";
        } else if (content.contains("修复") || content.contains("bug")) {
            return "BUGFIX";
        }
        return "NORMAL";
    }

    private int calculatePriority(WuwaNoticeResponse.GameNotice notice) {
        int priority = 0;

        if (notice.getRed() != null && notice.getRed() == 1) {
            priority += 100;
        }

        Long currentTime = System.currentTimeMillis();
        if (notice.getStartTimeMs() != null && notice.getStartTimeMs() <= currentTime) {
            priority += 25;
        }

        return priority;
    }

    private String getNoticeTitle(WuwaNoticeResponse.GameNotice notice) {
        if (notice.getTabTitle() != null && !notice.getTabTitle().trim().isEmpty()) {
            String rawTitle = notice.getTabTitle().trim();
            if (isValidTitle(rawTitle)) {
                return rawTitle.replace("\n", "");
            }
        }
        return extractTitleFromContent(notice.getContent());
    }

    private boolean isValidTitle(String title) {
        if (title.length() < 4) {
            return false;
        }
        String[] invalidPatterns = {
            "亲爱的漂泊者",
            "尊敬的玩家",
            "各位玩家",
            "亲爱的玩家",
            "亲爱的各位"
        };
        for (String pattern : invalidPatterns) {
            if (title.contains(pattern)) {
                return false;
            }
        }
        return true;
    }

    private String extractTitleFromContent(String contentHtml) {
        if (contentHtml == null || contentHtml.isEmpty()) {
            return "无标题";
        }

        try {
            String text = contentHtml.replaceAll("<[^>]*>", " ").replaceAll("\\s+", " ").trim();

            if (text.contains("自星海尽处回响")) {
                int index = text.indexOf("自星海尽处回响");
                int start = Math.max(0, index - 20);
                int end = Math.min(text.length(), index + 30);
                String extracted = text.substring(start, end).replace("感谢您的持续关注！", "").trim();
                if (!extracted.isEmpty()) {
                    return extracted.length() > 100 ? extracted.substring(0, 100) : extracted;
                }
            }

            String[] patterns = {
                "【([^】]+)】",
                "「([^」]+)」",
                "([^\\s]+\\s*版本\\s*介绍)",
                "(版本\\s*更新\\s*说明)",
                "(维护\\s*公告)",
                "(活动\\s*公告)"
            };

            for (String pattern : patterns) {
                java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
                java.util.regex.Matcher m = p.matcher(text);
                if (m.find()) {
                    String title = m.group(1) != null ? m.group(1) : m.group(0);
                    if (title != null && !title.isEmpty()) {
                        return title.length() > 100 ? title.substring(0, 100) : title;
                    }
                }
            }

            int firstDivEnd = contentHtml.indexOf("</div>");
            if (firstDivEnd > 0 && firstDivEnd < 200) {
                String firstLine = contentHtml.substring(0, firstDivEnd);
                firstLine = firstLine.replaceAll("<[^>]*>", "").trim();
                if (!firstLine.isEmpty()) {
                    return firstLine.length() > 100 ? firstLine.substring(0, 100) : firstLine;
                }
            }
        } catch (Exception e) {
            log.debug("提取标题失败", e);
        }

        return "公告";
    }

    public List<AnnouncementVO> getAnnouncementsByGameCode(String gameCode) {
        String cacheKey = CACHE_KEY_PREFIX + gameCode;

        try {
            String cached = redisTemplate.opsForValue().get(cacheKey);
            if (cached != null) {
                log.debug("从Redis缓存获取公告列表: {}", gameCode);
                return objectMapper.readValue(cached, new TypeReference<List<AnnouncementVO>>() {});
            }
        } catch (Exception e) {
            log.warn("读取Redis缓存失败: {}", e.getMessage());
        }

        List<Announcement> announcements;

        if (GAME_CODE_WUWA.equals(gameCode)) {
            Long currentTime = System.currentTimeMillis();
            announcements = announcementRepository.findByGameCodeAndStartTimeMsLessThanEqualAndEndTimeMsGreaterThanEqualOrderByPriorityDescCreateTimeDesc(
                    gameCode, currentTime, currentTime);
        } else {
            announcements = announcementRepository.findByGameCodeOrderByPriorityDescCreateTimeDesc(gameCode);
        }

        List<AnnouncementVO> result = announcements.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        try {
            redisTemplate.opsForValue().set(cacheKey, objectMapper.writeValueAsString(result), CACHE_EXPIRE_MINUTES, TimeUnit.MINUTES);
            log.debug("公告列表已缓存: {} 条目", result.size());
        } catch (Exception e) {
            log.warn("写入Redis缓存失败: {}", e.getMessage());
        }

        return result;
    }

    private void clearAnnouncementCache(String gameCode) {
        try {
            String cacheKey = CACHE_KEY_PREFIX + gameCode;
            redisTemplate.delete(cacheKey);
            log.debug("已清除公告缓存: {}", cacheKey);
        } catch (Exception e) {
            log.warn("清除Redis缓存失败: {}", e.getMessage());
        }
    }

    private AnnouncementVO convertToVO(Announcement announcement) {
        AnnouncementVO vo = new AnnouncementVO();
        vo.setAnnouncementId(announcement.getAnnouncementId());
        vo.setGameCode(announcement.getGameCode());
        vo.setTitle(announcement.getTitle());
        vo.setContentHtml(announcement.getContentHtml());
        vo.setTabBanner(announcement.getTabBanner());
        vo.setFoldBanner(announcement.getFoldBanner());
        vo.setIsRedDot(announcement.getIsRedDot());
        vo.setStartTimeMs(announcement.getStartTimeMs());
        vo.setEndTimeMs(announcement.getEndTimeMs());
        vo.setStartTimeStr(formatTimeStamp(announcement.getStartTimeMs()));
        vo.setEndTimeStr(formatTimeStamp(announcement.getEndTimeMs()));
        vo.setAnnouncementType(announcement.getAnnouncementType());
        vo.setPriority(announcement.getPriority());
        vo.setCreateTime(announcement.getCreateTime());
        return vo;
    }

    private String formatTimeStamp(Long timeStamp) {
        if (timeStamp == null) {
            return "";
        }
        Instant instant = Instant.ofEpochMilli(timeStamp);
        ZoneId zoneId = ZoneId.of("Asia/Shanghai");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(zoneId);
        return formatter.format(instant);
    }
}