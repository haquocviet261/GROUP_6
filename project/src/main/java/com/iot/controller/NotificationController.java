package com.iot.controller;

import com.iot.model.dto.response.ResponseObject;
import com.iot.services.imp.NotificationServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("api/notification")
public class NotificationController {
    @Autowired
    private NotificationServiceImp notificationServiceImp;

    @GetMapping("/all")
    public ResponseEntity<ResponseObject> getAllNotification() {
        return notificationServiceImp.getAllNotification();
    }

    @GetMapping("/number-unread-notification")
    public ResponseEntity<ResponseObject> getNumberOfUnreadNotification() {
        return notificationServiceImp.getNumberOfUnreadNotification();
    }

    @PostMapping("/update-status/all")
    public ResponseEntity<ResponseObject> updateAllNotificationStatus(@RequestParam(name = "status") String status) {
        return notificationServiceImp.updateAllNotificationStatus(status);
    }

    @PostMapping("/update-status/{notification_id}")
    public ResponseEntity<ResponseObject> updateNotificationStatus(@PathVariable Long notification_id, @RequestParam(name = "status") String status) {
        return notificationServiceImp.updateNotificationStatus(notification_id,status);
    }

    @GetMapping("/clear/all")
    public ResponseEntity<ResponseObject> clearAllNotificationByUserId(){
        return notificationServiceImp.clearAllNotificationByUserId();
    }

    @GetMapping("/clear/{notification_id}")
    public ResponseEntity<ResponseObject> clearNotificationId(@PathVariable Long notification_id){
        return notificationServiceImp.clearNotificationId(notification_id);
    }

    @GetMapping("/filter/{typeNotification}/{timePeriod}")
    public ResponseEntity<ResponseObject> getNotificationsByTimePeriod(@PathVariable(name = "typeNotification") String typeNotification,@PathVariable(name = "timePeriod") String timePeriod) {
        return notificationServiceImp.getNotificationsByTimePeriod(timePeriod,typeNotification);
    }

    @GetMapping("/get-count-by-date")
    public ResponseEntity<ResponseObject> getCountTypeNotificationBySpecificDay(
            @RequestParam(name = "specificDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date specificDate) {

        return notificationServiceImp.countNotificationsByType(specificDate);
    }

}
