package com.gamelover.gamedata.controller;

import com.gamelover.gamedata.service.AnnouncementService;
import com.gamelover.gamedata.vo.AnnouncementVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/announcement")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getAnnouncementList(
            @RequestParam(required = false, defaultValue = "WUWA") String gameCode) {

        List<AnnouncementVO> announcements = announcementService.getAnnouncementsByGameCode(gameCode);

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "success");
        result.put("data", announcements);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/sync")
    public ResponseEntity<Map<String, Object>> syncAnnouncements() {
        announcementService.syncWuwaAnnouncements();

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "同步成功");

        return ResponseEntity.ok(result);
    }
}
