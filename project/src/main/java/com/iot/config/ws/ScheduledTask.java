package com.iot.config.ws;

import com.iot.common.constant.CommonConstant;
import com.iot.common.utils.Validation;
import com.iot.model.entity.*;
import com.iot.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CompanyRepository companyRepository;


    @Scheduled(fixedRate = 300000)////15 minutes - = 5 * 60 *1000
    public void checkFoodExpired() {
        log.info("Checking for expired and soon-to-expire food items {}", dateFormat.format(new Date()));
        List<FoodItem> foodItems = foodItemRepository.findAll();
        for (FoodItem foodItem : foodItems) {
            Date currentExpirationDate = Validation.calculateExpirationDate(foodItem.getExpired_date(), foodItem.getUpdated_at());

            boolean expirationDateChanged = foodItem.getLastCheckedExpirationDate() == null ||
                    !foodItem.getLastCheckedExpirationDate().toInstant().equals(currentExpirationDate.toInstant());
            if (expirationDateChanged) {
                foodItem.setDaysBeforeExpiredNotified(null);
            }

            long daysLeft = Validation.calculateDaysLeft(foodItem.getExpired_date(), foodItem.getUpdated_at());
            String message = null;

            if (daysLeft == 0 && (foodItem.getDaysBeforeExpiredNotified() == null || foodItem.getDaysBeforeExpiredNotified() < 0)) {
                message = foodItem.getName() + " will expire today!";
                foodItem.setDaysBeforeExpiredNotified(0);
            } else if (daysLeft == 3 && (foodItem.getDaysBeforeExpiredNotified() == null || foodItem.getDaysBeforeExpiredNotified() < 3)) {
                message = foodItem.getName() + " will expire in less than 3 days!";
                foodItem.setDaysBeforeExpiredNotified(3);
            } else if (daysLeft == 2 && (foodItem.getDaysBeforeExpiredNotified() == null || foodItem.getDaysBeforeExpiredNotified() < 2)) {
                message = foodItem.getName() + " will expire in less than 2 days!";
                foodItem.setDaysBeforeExpiredNotified(2);
            } else if (daysLeft == 1 && (foodItem.getDaysBeforeExpiredNotified() == null || foodItem.getDaysBeforeExpiredNotified() < 1)) {
                message = foodItem.getName() + " will expire tomorrow!";
                foodItem.setDaysBeforeExpiredNotified(1);
            } else if (daysLeft < 0 && (foodItem.getDaysBeforeExpiredNotified() == null || foodItem.getDaysBeforeExpiredNotified() > 0)) {
                message = foodItem.getName() + " has expired!";
                foodItem.setDaysBeforeExpiredNotified(-1);
            }

            if (message != null) {
                foodItem.setLastNotifiedAt(new Date());

                foodItem.setLastCheckedExpirationDate(currentExpirationDate);
                foodItemRepository.save(foodItem);

                saveNotifications(CommonConstant.EXPIRATION_WARNING, message, foodItem.getCompanyId());
                template.convertAndSendToUser(foodItem.getCompanyId() != null ? foodItem.getCompanyId().toString() : "", "/topic/food-expired", message);
            }
        }
    }

    @Scheduled(fixedRate = 900000) //15 minutes - = 15 * 60 *1000
    public void checkFoodLowStock() {
        log.info("Check Food Low Stock {}", dateFormat.format(new java.util.Date()));

        List<FoodItem> foodItems = foodItemRepository.findAll();
        for (FoodItem foodItem : foodItems) {
            if (foodItem.getQuantity() < 1) {
                String message = "Warning: The stock for " + foodItem.getName() + " is running low. Please restock the inventory to avoid shortage.";
                saveNotifications(CommonConstant.FOOD_LOW_STOCK_WARNING, message, foodItem.getCompanyId());
                template.convertAndSendToUser(String.valueOf(foodItem.getCompanyId()), "/topic/weight", message);
            }
        }
    }

    @Scheduled(fixedRate = 10000) // 10s
    public void checkIncreasedWeight() {
        log.info("Checking newly added food items {}", dateFormat.format(new Date()));

        List<FoodItem> foodItems = foodItemRepository.findAll();

        for (FoodItem foodItem : foodItems) {
            LocalDateTime lastIncreaseTime = foodItem.getLastIncreaseTime() != null
                    ? foodItem.getLastIncreaseTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
                    : null;
            Double lastIncreaseWeight = foodItem.getLastIncreaseWeight();
            //Kiểm tra nếu đã qua 5 phút kể từ thời điểm LastIncreaseTime
            if (lastIncreaseTime != null && lastIncreaseWeight != null) {
                if (lastIncreaseTime.plusMinutes(5).isBefore(LocalDateTime.now())) {
                    Double currentWeight = foodItem.getQuantity();
                    if (currentWeight - lastIncreaseWeight >= 0.9) {
                        String message = "Food Item " + foodItem.getName() + " has been replenished. Please note the expiration date to ensure freshness.";
                        saveNotifications(CommonConstant.FOOD_ITEM_REPLENISHED_WARNING, message, foodItem.getCompanyId());
                        template.convertAndSendToUser(String.valueOf(foodItem.getCompanyId()), "/topic/food-replenish", message);
                        //Reset
                        foodItem.setLastIncreaseTime(null);
                        foodItem.setLastIncreaseWeight(null);
                        foodItemRepository.save(foodItem);
                    }
                }
            }
        }
    }

    @Scheduled(fixedRate = 900000) //15 minutes - = 15 * 60 *1000
    public void checkTemperatureHumidity() {
        log.info("Check Temperature and Humidity {}", dateFormat.format(new java.util.Date()));

        List<Company> companyList = companyRepository.findAll();
        for (Company company : companyList) {
            List<TemperatureHumidity> temperatureHumidityList = temperatureHumidityRepository.getAllByCompanyIdAndCreatedAtDesc(company.getId());
            if (temperatureHumidityList.isEmpty()) {
                return;
            }
            TemperatureHumidity temperatureHumidity = temperatureHumidityList.get(0);
            if (temperatureHumidity.getHumidity() > 95 || temperatureHumidity.getHumidity() < 50) {
                String message = "Alert humidity, please check your inventory !";
                saveNotifications(CommonConstant.HUMIDITY_WARNING, message, Math.toIntExact(company.getId()));
                template.convertAndSendToUser(String.valueOf(company.getId()), "/topic/humidity", message);
            }
            if (temperatureHumidity.getTemperature() > 8) {
                String message = "Alert temperature, please check your inventory !";
                saveNotifications(CommonConstant.TEMPERATURE_WARNING, message, Math.toIntExact(company.getId()));
                template.convertAndSendToUser(String.valueOf(company.getId()), "/topic/temperature", message);
            }
        }
    }

    private void saveNotifications(String typeNotification, String message, Integer companyId) {
        List<User> employees = userRepository.findByCompanyId(companyId);
        List<Notification> notifications = new ArrayList<>();
        for (User employee : employees) {
            notifications.add(Notification.builder().type_notification(typeNotification)
                    .message(message).userId(employee.getId()).status("unread").build());
        }
        notificationRepository.saveAll(notifications);
    }

    @Scheduled(cron = "0 0 0 * * MON")
    public void removeTemperatureHumidity() {
        log.info("Delete Temperature & Humidity {}", dateFormat.format(new java.util.Date()));
        temperatureHumidityRepository.deledeBydeviceId();
    }

    @Scheduled(cron = "0 0 22 * * ?")
    public void checkFoodItemWeightEndOfDay() {
        log.info("Check food inventory at the end of the day {}", new java.util.Date());

        List<FoodItem> foodItems = foodItemRepository.findAll();
        Map<Integer, List<FoodItem>> foodItemsByCompany = foodItems.stream()
                .collect(Collectors.groupingBy(FoodItem::getCompanyId));

        for (Map.Entry<Integer, List<FoodItem>> entry : foodItemsByCompany.entrySet()) {
            Integer companyId = entry.getKey();
            List<FoodItem> items = entry.getValue();

            StringBuilder message = new StringBuilder("Daily Stock Update: Here is the remaining quantity of each food item as of 10 PM:\n\n");
            for (FoodItem item : items) {
                message.append("- ").append(item.getName()).append(": ").append(item.getQuantity()).append("g\n");
            }
            message.append("\nCheck your inventory and plan for restocking if necessary.");

            saveNotifications(CommonConstant.FOOD_INVENTORY_END_OF_DAY_CHECK, message.toString(), companyId);
            template.convertAndSendToUser(String.valueOf(companyId), "/topic/daily-stock-update", message);
        }
    }

    @Scheduled(cron = "0 0 9 * * ?")
    public void checkForExpiredFood() {
        log.info("Check For Expired Food {}", new java.util.Date());

        List<FoodItem> foodItems = foodItemRepository.findAll();
        for (FoodItem foodItem : foodItems) {
            Date currentExpirationDate = Validation.calculateExpirationDate(foodItem.getExpired_date(), foodItem.getUpdated_at());
            if(currentExpirationDate.before(new Date())){
                String message = "Food Item " + foodItem.getName() + " has expired!";
                saveNotifications(CommonConstant.EXPIRATION_WARNING, message, foodItem.getCompanyId());
                template.convertAndSendToUser(String.valueOf(foodItem.getCompanyId()), "/topic/food-expired", message);
            }
        }

    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void removeNotification() {
        log.info("Delete Notification {}", dateFormat.format(new java.util.Date()));
        LocalDateTime tenDaysAgo = LocalDateTime.now().minusDays(10);
        notificationRepository.findNotificationsOlderThanTenDays(tenDaysAgo).forEach(notificationRepository::delete);
    }
}
