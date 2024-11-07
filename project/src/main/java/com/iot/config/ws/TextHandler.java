package com.iot.config.ws;

import com.iot.model.entity.TemperatureHumidity;
import com.iot.repositories.TemperatureHumidityRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class TextHandler extends TextWebSocketHandler {
    @Autowired
    SimpMessagingTemplate template;
    @Autowired
    private TemperatureHumidityRepository temperatureHumidityRepository;
    private final Set<WebSocketSession> sessions = Collections.newSetFromMap(new ConcurrentHashMap<>());
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("Message from device {}",payload);
        // Phân tích dữ liệu JSON
        JSONObject jsonObject = new JSONObject(payload);
        int companyId = jsonObject.getInt("company_id");
        double temperature = jsonObject.getDouble("temperature");
        double humidity = jsonObject.getDouble("humidity");
        JSONArray weightArray = jsonObject.getJSONArray("weight");
        List<Double> weights = new ArrayList<>();
        for (int i = 0; i < weightArray.length(); i++) {
            weights.add(weightArray.getDouble(i));
        }
        temperatureHumidityRepository.save(new TemperatureHumidity(null, temperature, humidity, companyId, new Date()));
        session.sendMessage(new TextMessage(jsonObject.toString()));
        checkTemperatureHumidity(temperature, humidity);

    }
    public void checkTemperatureHumidity(double temperature, double humidity) {
        if (humidity > 20 ) {
            template.convertAndSend("/topic/humidity", "Alert humidity, please check your inventory !");
        }
        if (temperature > 10) {
            template.convertAndSend("/topic/temperature", "Alert temperature please check your inventory !");
        }
    }
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        log.info("Session added: {}", session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        log.info("Session removed: {}", session.getId());
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        log.info("Message from handleMessage {}",message.getPayload());
        if (message instanceof TextMessage) {
            broadcastMessage(((TextMessage) message).getPayload());
        }
    }
    private void broadcastMessage(String message) throws Exception {
        for (WebSocketSession s : sessions) {
            if (s.isOpen()) {
                s.sendMessage(new TextMessage(message));
            }
        }
    }
}
