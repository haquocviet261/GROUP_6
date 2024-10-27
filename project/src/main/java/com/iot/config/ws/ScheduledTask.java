package com.iot.config.ws;

import com.iot.repositories.FoodItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
@Component
public class ScheduledTask {
    private static final Logger log = LoggerFactory.getLogger(ScheduledTask.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    @Autowired
    private SimpMessagingTemplate template;
    @Autowired
    private FoodItemRepository foodItemRepository;

    @Scheduled(fixedRate = 90000)
    public void suggestFoodAtFiveThirtyPM() {
        log.info("This time to suggest food for dinner now {}", dateFormat.format(new java.util.Date()));
        template.convertAndSend("/topic/dinner", "Time to suggest food for dinner");
    }
    @Scheduled(fixedRate = 270000)
    public void saveFoodEnday() {
        log.info("The time save foood now is {}", dateFormat.format(new java.util.Date()));
        template.convertAndSend("/topic/humidity", "Time to caculate the calories !");
    }
}
