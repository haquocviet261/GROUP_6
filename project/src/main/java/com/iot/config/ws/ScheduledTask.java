package com.iot.config.ws;

import com.iot.model.entity.FoodItem;
import com.iot.repositories.FoodItemRepository;
import com.iot.repositories.TemperatureHumidityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
    private TemperatureHumidityRepository temperatureHumidityRepository;

    @Scheduled(fixedRate = 90000)
    public void sendFoodExpired() {
        log.info("Checking for expired and soon-to-expire food items {}", dateFormat.format(new java.util.Date()));

        List<FoodItem> foodItems = foodItemRepository.findAll();
        Date currentDate = new Date();
        Calendar warningCal = Calendar.getInstance();
        warningCal.setTime(currentDate);
        warningCal.add(Calendar.DAY_OF_MONTH, 3);

        for (FoodItem foodItem : foodItems) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(foodItem.getCreated_at());
            cal.add(Calendar.DAY_OF_MONTH, foodItem.getExpired_date()); // Calculate the expiration date

            if (cal.getTime().before(currentDate)) {
                // Food item is expired
                template.convertAndSendToUser(foodItem.getCompanyId() != null ? foodItem.getCompanyId().toString() : "", "/topic/food", foodItem.getName() + " is expired!");
            } else if (cal.getTime().before(warningCal.getTime())) {
                // Food item will expire within the next 3 days
                template.convertAndSendToUser(foodItem.getCompanyId() != null ? foodItem.getCompanyId().toString() : "", "/topic/food", foodItem.getName() + " will expire in less than 3 days!");
            }
        }
    }

    @Scheduled(cron = "0 0 0 * * MON")
    public void removeTemperatureHumidity() {
        log.info("Delete Temperature & Humidity {}", dateFormat.format(new java.util.Date()));
        temperatureHumidityRepository.deledeBydeviceId();
    }
}
