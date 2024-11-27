package com.iot.services.imp;

import com.iot.common.utils.CommonUtils;
import com.iot.common.utils.Validation;
import com.iot.model.dto.response.NotificationResponse;
import com.iot.model.dto.response.ResponseObject;
import com.iot.model.entity.Notification;
import com.iot.model.entity.User;
import com.iot.repositories.NotificationRepository;
import com.iot.services.interfaces.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

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
        Notification notification = findNotificationOrThrow(notificationId);
        notification.setStatus(status);
        notificationRepository.save(notification);
        return ResponseEntity.ok(new ResponseObject(Validation.OK, "Notice has been " + status, notification));
    }

    @Override
    public ResponseEntity<ResponseObject> clearAllNotificationByUserId() {
        User user = CommonUtils.getUserInformationLogin();
        int deletedCount = notificationRepository.markAllNotificationsAsDeleted(user.getId(), new Date());
        return ResponseEntity.ok(new ResponseObject(Validation.OK, "Cleared " + deletedCount + " notices successfully!", ""));
    }

    @Override
    public ResponseEntity<ResponseObject> clearNotificationId(Long notificationId) {
        Notification notification = findNotificationOrThrow(notificationId);
        notification.setDeleted_at(new Date());
        notificationRepository.save(notification); // Batch size nhỏ, chỉ một bản ghi
        return ResponseEntity.ok(new ResponseObject(Validation.OK, "Success", ""));
    }

    @Override
    public ResponseEntity<ResponseObject> updateAllNotificationStatus(String status) {
        User user = CommonUtils.getUserInformationLogin();
        int updatedCount = notificationRepository.updateAllNotificationsStatus(user.getId(), status);
        return ResponseEntity.ok(new ResponseObject(Validation.OK, "Updated " + updatedCount + " notices successfully!", ""));
    }

    private Notification findNotificationOrThrow(Long notificationId) {
        return notificationRepository.getNotificationById(notificationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notification not found"));
    }


    @Override
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

    @Override
    public ResponseEntity<ResponseObject> countNotificationsByType(Date specificDate){
        User user = CommonUtils.getUserInformationLogin();
        Date startOfDay = Validation.startOfDay(specificDate);
        Date endOfDay = Validation.endOfDay(specificDate);

        List<Notification> notifications = notificationRepository.getAllByCompanyIdAndDateRange(user.getId(), startOfDay, endOfDay);
        List<String> notificationTypes = Arrays.asList(
                "EXPIRATION_WARNING",
                "TEMPERATURE_WARNING",
                "HUMIDITY_WARNING",
                "FOOD_LOW_STOCK_WARNING",
                "FOOD_INVENTORY_END_OF_DAY",
                "UPDATE_FOOD_ITEM",
                "FOOD_ITEM_REPLENISHED_WARNING"
        );

        List<NotificationResponse> notificationResponses = notificationTypes.stream()
                .map(type -> new NotificationResponse(
                        type,
                        notifications.stream()
                                .filter(n -> n.getType_notification().equals(type))
                                .count()
                ))
                .toList();
        return ResponseEntity.ok(new ResponseObject(Validation.OK, "Success!",notificationResponses));
    }

}
