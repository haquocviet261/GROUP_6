package com.iot.services.interfaces;

import com.iot.model.dto.response.ResponseObject;
import org.springframework.http.ResponseEntity;

import java.util.Date;

public interface NotificationService {
    ResponseEntity<ResponseObject> getAllNotification();

    ResponseEntity<ResponseObject> getNumberOfUnreadNotification();

    ResponseEntity<ResponseObject> updateNotificationStatus(Long notificationId, String status);

    ResponseEntity<ResponseObject> clearAllNotificationByUserId();

    ResponseEntity<ResponseObject> clearNotificationId(Long notificationId);

    ResponseEntity<ResponseObject> updateAllNotificationStatus(String status);

    ResponseEntity<ResponseObject> getNotificationsByTimePeriod(String timePeriod, String typeNotification);

    ResponseEntity<ResponseObject> countNotificationsByType(Date specificDate);
}
