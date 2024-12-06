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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
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
    @Autowired
    InventoryLogRepository inventoryLogRepository;


    @Scheduled(fixedRate = 300000)////15 minutes - = 5 * 60 *1000
    public void checkFoodExpired() {
        log.info("Checking for expired and soon-to-expire food items {}", dateFormat.format(new Date()));
        List<FoodItem> foodItems = foodItemRepository.findAll();
        for (FoodItem foodItem : foodItems) {
            if (foodItem.getExpired_date() == null) {
                continue;
            }
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

    @Scheduled(fixedRate = 5000) //15 minutes - = 15 * 60 *1000
    public void checkFoodLowStock() {
        log.info("Check Food Low Stock {}", dateFormat.format(new java.util.Date()));

        List<FoodItem> foodItems = foodItemRepository.findAll();
        for (FoodItem foodItem : foodItems) {
            if (foodItem.getQuantity() != null && foodItem.getQuantity() < 1) {
                if (!foodItem.getIsLowStock()) {
                    String message = "Warning: The stock for " + foodItem.getName() + " is running low. Please restock the inventory to avoid shortage.";
                    saveNotifications(CommonConstant.FOOD_LOW_STOCK_WARNING, message, foodItem.getCompanyId());
                    template.convertAndSendToUser(String.valueOf(foodItem.getCompanyId()), "/topic/weight", message);
                    foodItem.setIsLowStock(true);
                    foodItemRepository.save(foodItem);
                }
            } else{
                foodItem.setIsLowStock(false);
                foodItemRepository.save(foodItem);
            }
        }
    }

    @Scheduled(fixedRate = 10000) // 10s
    public void checkIncreasedWeight() {
        log.info("Checking newly added food items {}", dateFormat.format(new Date()));

        List<FoodItem> foodItems = foodItemRepository.findAll();

        for (FoodItem foodItem : foodItems) {
            Double currentWeight = foodItem.getQuantity();

            if (foodItem.getLastIncreaseWeight() == null) {
                foodItem.setLastIncreaseWeight(currentWeight);
                foodItemRepository.save(foodItem);
                log.info("Initialized LastIncreaseWeight for food item {}", foodItem.getName());
                continue;
            }

            Double lastIncreaseWeight = foodItem.getLastIncreaseWeight();

            if (currentWeight > lastIncreaseWeight) {
                Double addedQuantity = currentWeight - lastIncreaseWeight;

                if (addedQuantity >= 0.9) {
                    if (foodItem.getLastIncreaseTime() == null) {
                        foodItem.setLastIncreaseTime(new Date());
                        foodItemRepository.save(foodItem);
                        log.info("Initialized LastIncreaseTime for food item {}", foodItem.getName());
                        continue;
                    }

                    LocalDateTime lastIncreaseTime = foodItem.getLastIncreaseTime()
                            .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    if (lastIncreaseTime.plusMinutes(5).isBefore(LocalDateTime.now())) {
                        Date today = java.sql.Date.valueOf(LocalDate.now());
                        Date startOfDay = Validation.startOfDay(today);
                        Date endOfDay = Validation.endOfDay(today);

                        Optional<InventoryLog> logOptional = inventoryLogRepository.findByFoodItemIdAndCreatedAt(
                                foodItem.getId(), foodItem.getName(), startOfDay, endOfDay);

                        InventoryLog log;
                        if (logOptional.isPresent()) {
                            log = logOptional.get();
                            Double openingQuantity = log.getClosingQuantity() != null ? log.getClosingQuantity() : 0.0;
                            double totalAddedQuantity = log.getAddedQuantity() != null ? log.getAddedQuantity() : 0.0;

                            Double consumedQuantity = openingQuantity - currentWeight + totalAddedQuantity;
                            log.setConsumedQuantity(consumedQuantity);

                            log.setAddedQuantity(log.getAddedQuantity() + addedQuantity);
                            log.setClosingQuantity(currentWeight);
                        } else {
                            log = new InventoryLog();
                            log.setFoodItemId(Math.toIntExact(foodItem.getId()));
                            log.setCompanyId(foodItem.getCompanyId());
                            log.setAddedQuantity(addedQuantity);
                            log.setClosingQuantity(currentWeight);
                            log.setCreated_at(today);
                            log.setFoodName(foodItem.getName());
                            log.setUnit(foodItem.getType_unit());

                            log.setConsumedQuantity(addedQuantity);
                        }

                        inventoryLogRepository.save(log);

                        foodItem.setLastIncreaseTime(new Date());
                        foodItem.setLastIncreaseWeight(currentWeight);
                        foodItemRepository.save(foodItem);

                        String message = "Food Item " + foodItem.getName() + " has been replenished. Total added today: "
                                + log.getAddedQuantity() + " kg.";
                        saveNotifications(CommonConstant.FOOD_ITEM_REPLENISHED_WARNING, message, foodItem.getCompanyId());
                        template.convertAndSendToUser(String.valueOf(foodItem.getCompanyId()), "/topic/food-replenish", message);
                    }
                }
            } else {
                foodItem.setLastIncreaseWeight(currentWeight);
                foodItem.setLastIncreaseTime(null);
                foodItemRepository.save(foodItem);
                log.info("Updated LastIncreaseWeight for food item {} due to consumption", foodItem.getName());
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
                Date today = java.sql.Date.valueOf(LocalDate.now());
                Date startOfDay = Validation.startOfDay(today);
                Date endOfDay = Validation.endOfDay(today);

                Optional<InventoryLog> logOptional = inventoryLogRepository.findByFoodItemIdAndCreatedAt(
                        item.getId(), item.getName(), startOfDay, endOfDay);
                if (logOptional.isEmpty()) {
                    inventoryLogRepository.save(InventoryLog.builder().foodItemId(Math.toIntExact(item.getId()))
                            .unit(item.getType_unit())
                            .closingQuantity(item.getQuantity())
                            .companyId(item.getCompanyId())
                            .created_at(new Date())
                            .foodName(item.getName())
                            .consumedQuantity(0.0)
                            .addedQuantity(0.0).build());
                } else {
                    logOptional.get().setClosingQuantity(item.getQuantity());
                    inventoryLogRepository.save(logOptional.get());
                }


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
            if (currentExpirationDate.before(new Date())) {
                String message = "Food Item " + foodItem.getName() + " has expired!";
                saveNotifications(CommonConstant.EXPIRATION_WARNING, message, foodItem.getCompanyId());
                template.convertAndSendToUser(String.valueOf(foodItem.getCompanyId()), "/topic/food-expired", message);
            }
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void removeOldData() {
        log.info("Start clearing old data at {}", dateFormat.format(new java.util.Date()));
        LocalDateTime twelveDaysAgo = LocalDateTime.now().minusDays(12);


        int deletedTempHumidCount = temperatureHumidityRepository.deleteTemperatureHumidityOlderThan(twelveDaysAgo);
        log.info("Deleted {} records from Temperature & Humidity.", deletedTempHumidCount);

        int deletedInventoryLogCount = inventoryLogRepository.deleteInventoryLogsOlderThan(twelveDaysAgo);
        log.info("Deleted {} records from Inventory Log.", deletedInventoryLogCount);

        int deletedNotificationCount = notificationRepository.deleteNotificationsOlderThan(twelveDaysAgo);
        log.info("Deleted {} records from Notifications.", deletedNotificationCount);

        log.info("Finished clearing old data.");
    }
}
