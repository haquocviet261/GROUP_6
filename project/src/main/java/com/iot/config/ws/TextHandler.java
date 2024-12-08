package com.iot.config.ws;

import com.iot.common.constant.CommonConstant;
import com.iot.model.entity.*;
import com.iot.repositories.*;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.util.ObjectUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TextHandler extends TextWebSocketHandler {

    @Autowired
    private TemperatureHumidityRepository temperatureHumidityRepository;
    @Autowired
    private FoodItemRepository foodItemRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private SimpMessagingTemplate template;

    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();

    private TemperatureHumidity tempHumidityData = null;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public TextHandler() {
        // Schedule the periodic task to save data every 20 minutes
        scheduler.scheduleAtFixedRate(this::saveTemperatureHumidityData, 0, 20, TimeUnit.MINUTES);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        log.info("Session added: {}", session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        log.info("Session removed: {}", session.getId());
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {
        String payload = (String) message.getPayload();
        log.info("Message from device: {}", payload);

        JSONObject jsonObject = new JSONObject(payload);
        int companyId = jsonObject.getInt("company_id");
        double temperature = jsonObject.getDouble("temperature");
        double humidity = jsonObject.getDouble("humidity");
        JSONArray weightArray = jsonObject.getJSONArray("weights");

        // Save temperature and humidity data
        saveTemperatureHumidityData(temperature, companyId, humidity);

        // Process food items and update quantities
        processFoodItems(weightArray);

        // Check if any food items are low in stock
        checkFoodLowStock();

        // Broadcast message to WebSocket clients
        if (message instanceof TextMessage) {
            broadcastMessage(((TextMessage) message).getPayload());
        }
    }

    private void saveTemperatureHumidityData(double temperature, int companyId, double humidity) {
        tempHumidityData = new TemperatureHumidity(temperature, humidity, companyId, new Date());
        temperatureHumidityRepository.save(tempHumidityData);
        log.info("Temperature and humidity data saved: {}", tempHumidityData);
    }

    private void processFoodItems(JSONArray weightArray) {
        for (int i = 0; i < weightArray.length(); i++) {
            JSONObject weightObject = weightArray.getJSONObject(i);
            int foodItemId = weightObject.getInt("foodItemId");
            double weight = weightObject.getDouble("weight");

            Optional<FoodItem> foodItemOptional = foodItemRepository.findById((long) foodItemId);
            foodItemOptional.ifPresent(foodItem -> updateFoodItemQuantity(foodItem, weight));
        }
    }

    private void checkFoodLowStock() {
        List<FoodItem> foodItems = foodItemRepository.findAll();
        foodItems.forEach(foodItem -> {
            if (foodItem.getQuantity() != null) {
                if (foodItem.getIsLowStock() == null) {
                    foodItem.setIsLowStock(true);
                    foodItemRepository.save(foodItem);
                } else {
                    if (foodItem.getQuantity() < 1 && !foodItem.getIsLowStock()) {
                        // Notify if stock is low
                        String message = "Warning: The stock for " + foodItem.getName() + " is running low.";
                        saveNotifications(CommonConstant.FOOD_LOW_STOCK_WARNING, message, foodItem.getCompanyId());
                        template.convertAndSendToUser(String.valueOf(foodItem.getCompanyId()), "/topic/weight", message);
                        foodItem.setIsLowStock(true);
                        foodItemRepository.save(foodItem);
                    } else if (foodItem.getQuantity() >= 1) {
                        foodItem.setIsLowStock(false);
                        foodItemRepository.save(foodItem);
                    }
                }
            }
        });
    }

    private void saveNotifications(String typeNotification, String message, Integer companyId) {
        List<User> employees = userRepository.findByCompanyId(companyId);
        List<Notification> notifications = new ArrayList<>();
        for (User employee : employees) {
            notifications.add(new Notification(typeNotification, message, employee.getId(), "unread"));
        }
        notificationRepository.saveAll(notifications);
    }

    private void updateFoodItemQuantity(FoodItem foodItem, double newQuantity) {
        if (ObjectUtils.isEmpty(foodItem.getQuantity())) {
            foodItem.setQuantity(newQuantity);
        } else {
            double currentQuantity = foodItem.getQuantity();
            if (newQuantity - currentQuantity >= 1.0) {
                foodItem.setLastIncreaseTime(new Date());
                foodItem.setLastIncreaseWeight(currentQuantity);
            }
            foodItem.setQuantity(newQuantity);
        }
        foodItemRepository.save(foodItem);
    }

    private void broadcastMessage(String message) {
        sessions.forEach(session -> {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(message));
                } catch (Exception e) {
                    log.error("Failed to send message to session {}: {}", session.getId(), e.getMessage());
                }
            }
        });
    }

    private void checkTemperatureHumidity() {
        List<Company> companyList = companyRepository.findAll();
        companyList.forEach(company -> {
            List<TemperatureHumidity> temperatureHumidityList = temperatureHumidityRepository.getAllByCompanyIdAndCreatedAtDesc(company.getId());
            if (!temperatureHumidityList.isEmpty()) {
                TemperatureHumidity temperatureHumidity = temperatureHumidityList.get(0);
                if (temperatureHumidity.getHumidity() > 95 || temperatureHumidity.getHumidity() < 50) {
                    sendHumidityWarning(company.getId());
                }
                if (temperatureHumidity.getTemperature() > 8) {
                    sendTemperatureWarning(company.getId());
                }
            }
        });
    }

    private void sendHumidityWarning(Long companyId) {
        String message = "Alert: Humidity levels are out of range. Please check your inventory!";
        saveNotifications(CommonConstant.HUMIDITY_WARNING, message, Math.toIntExact(companyId));
        template.convertAndSendToUser(String.valueOf(companyId), "/topic/humidity", message);
    }

    private void sendTemperatureWarning(Long companyId) {
        String message = "Alert: Temperature levels are out of range. Please check your inventory!";
        saveNotifications(CommonConstant.TEMPERATURE_WARNING, message, Math.toIntExact(companyId));
        template.convertAndSendToUser(String.valueOf(companyId), "/topic/temperature", message);
    }

    private void saveTemperatureHumidityData() {
        if (tempHumidityData != null) {
            temperatureHumidityRepository.save(tempHumidityData);
            log.info("Data saved to repository: {}", tempHumidityData);
            tempHumidityData = null;  // Reset data after saving
        }
        checkTemperatureHumidity();
    }
}
