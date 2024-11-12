package com.iot.repositories;

import com.iot.model.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query("SELECT n " +
            "FROM Notification n " +
            "JOIN User u ON n.userId = u.id " +
            "Where u.id = :userId")
    List<Notification> getAllNotification(@Param("userId") Long userId);

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.userId = :userId AND n.status = 'unread'")
    long countUnreadNotificationsByUserId(@Param("userId") Long userId);

    @Query("SELECT n FROM Notification n WHERE n.id = :notification_id")
    Optional<Notification> getNotificationById(@Param("notification_id") Long notification_id);

    @Query("SELECT n FROM Notification n WHERE n.created_at >= :start AND n.created_at < :end AND n.userId = :userId AND n.type_notification LIKE %:type_notification%")
    List<Notification> getNotificationsByTimePeriodAndType(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end,
                                                           @Param("userId") Long userId, @Param("type_notification") String type_notification);

    @Query("SELECT n FROM Notification n WHERE n.created_at <= :tenDaysAgo")
    List<Notification> findNotificationsOlderThanTenDays(@Param("tenDaysAgo") LocalDateTime tenDaysAgo);
}
