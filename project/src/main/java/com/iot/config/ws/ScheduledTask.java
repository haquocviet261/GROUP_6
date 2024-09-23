package com.iot.config.ws;

import com.iot.model.entity.FoodItem;
import com.iot.repositories.FoodItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

@Component
public class ScheduledTask {
    private static final Logger log = LoggerFactory.getLogger(ScheduledTask.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    @Autowired
    private SimpMessagingTemplate template;
    @Autowired
    private FoodItemRepository foodItemRepository;

    //@Scheduled(cron = "0 0 10 * * ?")
    @Scheduled(fixedRate = 60000)
    public void suggestFoodAtTenAM() {
        log.info("This time to suggest food for lunch now {}", dateFormat.format(new java.util.Date()));
        template.convertAndSend("/topic/lunch", "Time to suggest food for lunch");
    }
   // @Scheduled(cron = "0 30 17 * * ?")
   @Scheduled(fixedRate = 90000)
    public void suggestFoodAtFiveThirtyPM() {
        log.info("This time to suggest food for dinner now {}", dateFormat.format(new java.util.Date()));
        template.convertAndSend("/topic/dinner", "Time to suggest food for dinner");
    }
    @Scheduled(fixedRate = 270000)
    public void saveFoodEnday() {
        log.info("The time save foood now is {}", dateFormat.format(new java.util.Date()));
        template.convertAndSend("/topic/save_food_end_day", "Time to caculate the calories !");
    }
    @Scheduled(fixedRate = 240000)
    public void caCulateCalories() {
        log.info("The time to caculate calories now is {}", dateFormat.format(new java.util.Date()));
        template.convertAndSend("/topic/caculate_calories", "Time to caculate the calories !");
    }
}
