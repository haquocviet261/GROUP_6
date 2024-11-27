package com.iot.repositories;

import com.iot.model.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query("DELETE FROM Notification n WHERE n.created_at < :date")
    int deleteNotificationsOlderThan(@Param("date") LocalDateTime date);

    @Query("SELECT n " +
            "FROM Notification n " +
            "JOIN User u ON n.userId = u.id " +
            "Where u.id = :userId AND n.deleted_at IS NULL")
    List<Notification> getAllNotification(@Param("userId") Long userId);

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.userId = :userId AND n.status = 'unread' AND n.deleted_at IS NULL")
    long countUnreadNotificationsByUserId(@Param("userId") Long userId);

    @Query("SELECT n FROM Notification n WHERE n.id = :notification_id AND n.deleted_at IS NULL")
    Optional<Notification> getNotificationById(@Param("notification_id") Long notification_id);

    @Query("SELECT n FROM Notification n WHERE n.created_at >= :start " +
            "AND n.created_at < :end AND n.userId = :userId AND n.type_notification LIKE %:type_notification% AND n.deleted_at IS NULL")
    List<Notification> getNotificationsByTimePeriodAndType(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end,
                                                           @Param("userId") Long userId, @Param("type_notification") String type_notification);

    @Query("SELECT n FROM Notification n WHERE n.userId = :userId AND n.created_at BETWEEN :startDate AND :endDate")
    List<Notification> getAllByCompanyIdAndDateRange(
            @Param("userId") Long userId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );

    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.deleted_at = :deletedAt WHERE n.userId = :userId")
    int markAllNotificationsAsDeleted(@Param("userId") Long userId, @Param("deletedAt") Date deletedAt);

    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.status = :status WHERE n.userId = :userId")
    int updateAllNotificationsStatus(@Param("userId") Long userId, @Param("status") String status);
}
