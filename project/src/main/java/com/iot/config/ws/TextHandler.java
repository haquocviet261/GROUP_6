package com.iot.config.ws;

import com.iot.model.entity.FoodItem;
import com.iot.model.entity.TemperatureHumidity;
import com.iot.repositories.FoodItemRepository;
import com.iot.repositories.TemperatureHumidityRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final Set<WebSocketSession> sessions = Collections.newSetFromMap(new ConcurrentHashMap<>());

    private TemperatureHumidity tempHumidityData = null;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public TextHandler() {
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
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String payload = (String) message.getPayload();
        log.info("Message from device {}", payload);

        JSONObject jsonObject = new JSONObject(payload);

        int companyId = jsonObject.getInt("company_id");
        double temperature = jsonObject.getDouble("temperature");
        double humidity = jsonObject.getDouble("humidity");
        JSONArray weightArray = jsonObject.getJSONArray("weights");

        tempHumidityData = TemperatureHumidity.builder()
                .temperature(temperature)
                .companyId(companyId)
                .humidity(humidity)
                .created_at(new Date())
                .build();

        for (int i = 0; i < weightArray.length(); i++) {
            JSONObject weightObject = weightArray.getJSONObject(i);
            int foodItemId = weightObject.getInt("foodItemId");
            double weight = weightObject.getDouble("weight");

            Optional<FoodItem> foodItemOptional = foodItemRepository.findById((long) foodItemId);
            foodItemOptional.ifPresent(foodItem -> updateFoodItemQuantity(foodItem, weight));
        }

        if (message instanceof TextMessage) {
            broadcastMessage(((TextMessage) message).getPayload());
        }
    }

    private void saveTemperatureHumidityData() {
        if (tempHumidityData != null) {
            temperatureHumidityRepository.save(tempHumidityData);
            log.info("Data saved to repository: {}", tempHumidityData);
            tempHumidityData = null;  // Reset sau khi lưu
        }
    }

    private void updateFoodItemQuantity(FoodItem foodItem, Double newQuantity) {
        double currentQuantity = foodItem.getQuantity();
        double difference = newQuantity - currentQuantity;
        if (difference >= 1.0) {
            foodItem.setLastIncreaseTime(new Date());
            foodItem.setLastIncreaseWeight(currentQuantity);
        }
        foodItem.setQuantity(newQuantity);
        foodItemRepository.save(foodItem);
    }

    private void broadcastMessage(String message) throws Exception {
        for (WebSocketSession s : sessions) {
            if (s.isOpen()) {
                s.sendMessage(new TextMessage(message));
            }
        }
    }
}
