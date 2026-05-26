package com.gamelover.gamedata.task;

import com.gamelover.gamedata.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnnouncementSyncTask {

    private final AnnouncementService announcementService;

    @Scheduled(cron = "0 */5 * * * ?")
    public void syncWuwaAnnouncements() {
        log.info("开始同步鸣潮公告...");
        announcementService.syncWuwaAnnouncements();
    }
}