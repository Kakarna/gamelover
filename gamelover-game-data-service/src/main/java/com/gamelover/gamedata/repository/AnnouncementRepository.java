package com.gamelover.gamedata.repository;

import com.gamelover.gamedata.entity.Announcement;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnnouncementRepository extends MongoRepository<Announcement, String> {

    Optional<Announcement> findByAnnouncementId(String announcementId);

    List<Announcement> findByGameCodeOrderByPriorityDescCreateTimeDesc(String gameCode);

    List<Announcement> findByGameCodeAndStartTimeMsLessThanEqualAndEndTimeMsGreaterThanEqualOrderByPriorityDesc(
            String gameCode, Long currentTime1, Long currentTime2);

    List<Announcement> findByGameCodeAndStartTimeMsLessThanEqualAndEndTimeMsGreaterThanEqualOrderByPriorityDescCreateTimeDesc(
            String gameCode, Long currentTime1, Long currentTime2);

    void deleteByAnnouncementId(String announcementId);
}