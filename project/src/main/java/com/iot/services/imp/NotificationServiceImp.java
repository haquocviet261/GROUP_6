package com.iot.services.imp;

import com.iot.common.utils.CommonUtils;
import com.iot.common.utils.Validation;
import com.iot.model.dto.response.ResponseObject;
import com.iot.model.entity.Notification;
import com.iot.model.entity.User;
import com.iot.repositories.NotificationRepository;
import com.iot.services.interfaces.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationServiceImp implements NotificationService {
    @Autowired
    NotificationRepository notificationRepository;

    @Override
    public ResponseEntity<ResponseObject> getAllNotification() {
        User user = CommonUtils.getUserInformationLogin();
        return ResponseEntity.ok(new ResponseObject(Validation.OK, "Success !!!", notificationRepository.getAllNotification(user.getId())));
    }

    @Override
    public ResponseEntity<ResponseObject> getNumberOfUnreadNotification() {
        User user = CommonUtils.getUserInformationLogin();
        return ResponseEntity.ok(new ResponseObject(Validation.OK, "Success !!!", notificationRepository.countUnreadNotificationsByUserId(user.getId())));
    }

    @Override
    public ResponseEntity<ResponseObject> updateNotificationStatus(Long notificationId, String status) {
        Optional<Notification> notificationOptional = notificationRepository.getNotificationById(notificationId);
        if (notificationOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(Validation.FAIL, "Not found", ""));
        }
        Notification notification = notificationOptional.get();
        notification.setStatus(status);
        return ResponseEntity.ok(new ResponseObject(Validation.OK, "Notice has been " + status, notificationRepository.save(notification)));
    }


    @Override
    public ResponseEntity<ResponseObject> clearAllNotificationByUserId() {
        User user = CommonUtils.getUserInformationLogin();
        List<Notification> notifications = notificationRepository.getAllNotification(user.getId());
        for (Notification notification : notifications) {
            notificationRepository.delete(notification);
        }
        return ResponseEntity.ok(new ResponseObject(Validation.OK, "Clear notice success!", ""));
    }

    @Override
    public ResponseEntity<ResponseObject> clearNotificationId(Long notificationId) {
        Optional<Notification> notificationOptional = notificationRepository.getNotificationById(notificationId);
        if (notificationOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(Validation.FAIL, "Not found", ""));
        }
        Notification notification = notificationOptional.get();
        notificationRepository.delete(notification);
        return ResponseEntity.ok(new ResponseObject(Validation.OK, "Success", ""));
    }

    @Override
    public ResponseEntity<ResponseObject> updateAllNotificationStatus(String status) {
        User user = CommonUtils.getUserInformationLogin();
        List<Notification> notifications = notificationRepository.getAllNotification(user.getId());
        for (Notification notification : notifications) {
            notification.setStatus(status);
            notificationRepository.save(notification);
        }
        return ResponseEntity.ok(new ResponseObject(Validation.OK, "Mark notice success!", ""));
    }

    public ResponseEntity<ResponseObject> getNotificationsByTimePeriod(String timePeriod, String typeNotification) {
        User user = CommonUtils.getUserInformationLogin();
        LocalDateTime start;
        LocalDateTime end = LocalDateTime.now();

        switch (timePeriod.toLowerCase()) {
            case "today":
                start = LocalDateTime.now().toLocalDate().atStartOfDay();
                break;
            case "yesterday":
                start = LocalDateTime.now().minusDays(1).toLocalDate().atStartOfDay();
                end = start.plusDays(1);
                break;
            case "this-week":
                start = LocalDateTime.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).toLocalDate().atStartOfDay();
                end = LocalDateTime.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).toLocalDate().atStartOfDay().plusDays(1);
                break;
            case "last-7-days":
                start = LocalDateTime.now().minusDays(7);
                break;
            default:
                start = LocalDateTime.of(1970, 1, 1, 0, 0);
                break;
        }

        if (typeNotification.equals("all")) {
            typeNotification = "";
        }
        return ResponseEntity.ok(new ResponseObject(Validation.OK, "Success!",
                notificationRepository.getNotificationsByTimePeriodAndType(start, end, user.getId(), typeNotification)));
    }

}
