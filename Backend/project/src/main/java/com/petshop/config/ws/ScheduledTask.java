package com.petshop.config.ws;

import com.petshop.model.dto.request.DeviceItemRequest;
import com.petshop.model.dto.request.FoodItemRequest;
import com.petshop.model.dto.response.DeviceItemResponse;
import com.petshop.model.dto.response.FoodItemResponse;
import com.petshop.model.entity.DeviceItem;
import com.petshop.model.entity.FoodItem;
import com.petshop.model.entity.User;
import com.petshop.repositories.DeviceItemRepository;
import com.petshop.repositories.FoodItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class ScheduledTask {
    private static final Logger log = LoggerFactory.getLogger(ScheduledTask.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    @Autowired
    private SimpMessagingTemplate template;
    @Autowired
    private FoodItemRepository foodItemRepository;
    @Autowired
    private DeviceItemRepository deviceItemRepository;

    @Scheduled(fixedRate = 30000)
    public void sendPeriodicMessages() {
            Date currentDate = Date.valueOf(LocalDate.now());
            Date expiryDate = new Date(currentDate.getTime() - (24 * 60 * 60 * 1000));
            List<FoodItem> foodItemList = foodItemRepository.getFoodItemsByDeviceItemID(expiryDate);
            if (!foodItemList.isEmpty()) {
                for (int i = 0; i < foodItemList.size() ; i++) {
                    template.convertAndSendToUser(String.valueOf(foodItemList.get(i).getDeviceItem().getUser().getUser_id()),"/topic/expiration", "Some foods in your Fridge are expired");
                }
            }
        template.convertAndSend("/topic/expiration", "Some foods in your Fridge are expired");
    }

    //@Scheduled(cron = "0 0 10 * * ?")
    @Scheduled(fixedRate = 60000)
    public void suggestFoodAtTenAM() {
        log.info("The time is now {}", dateFormat.format(new java.util.Date()));
        template.convertAndSend("/topic/lunch", "Time to suggest food for lunch");
    }
   // @Scheduled(cron = "0 30 17 * * ?")
   @Scheduled(fixedRate = 90000)
    public void suggestFoodAtFiveThirtyPM() {
        log.info("The time is now {}", dateFormat.format(new java.util.Date()));
        template.convertAndSend("/topic/dinner", "Time to suggest food for dinner");
    }
}
