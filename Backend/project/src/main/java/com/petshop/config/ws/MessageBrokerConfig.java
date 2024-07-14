package com.petshop.config.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.petshop.common.constant.Role;
import com.petshop.repositories.TokenRepository;
import com.petshop.services.imp.JwtServiceImp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.converter.DefaultContentTypeResolver;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
public class MessageBrokerConfig implements WebSocketMessageBrokerConfigurer {
    @Autowired
    private JwtServiceImp jwtServiceImp;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        log.info("Registered Stomp endpoint");
        registry.addEndpoint("/ws").setAllowedOrigins("*");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");// for Message Mapping
        registry.enableSimpleBroker("/user");
        registry.setUserDestinationPrefix("/user");
        registry.enableSimpleBroker("/topic");
    }

    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        DefaultContentTypeResolver resolver = new DefaultContentTypeResolver();
        resolver.setDefaultMimeType(MimeTypeUtils.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.findAndRegisterModules();  // Register all discovered modules

        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(objectMapper);
        converter.setContentTypeResolver(resolver);
        messageConverters.add(converter);

        messageConverters.add(new StringMessageConverter());

        return false;  // Return false to let Spring add default converters too
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String jwtToken = accessor.getFirstNativeHeader("Authorization");
                    if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
                        String jwt = jwtToken.substring(7);
                        String username = jwtServiceImp.extractUsername(jwt);
                        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                            boolean isTokenValid = tokenRepository.findByToken(jwt)
                                    .map(t -> !t.isExpired() && !t.isRevoked())
                                    .orElse(false);
                            if (jwtServiceImp.isTokenValid(jwt, userDetails) && isTokenValid) {
                                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                        userDetails, null, userDetails.getAuthorities()
                                );
                                SecurityContextHolder.getContext().setAuthentication(authentication);
                            } else {
                                log.warn("Invalid or expired token");
                                throw new IllegalStateException("Invalid or expired token");
                            }
                        } else {
                            log.warn("Username is null or user is already authenticated");
                            throw new IllegalStateException("Username is null or user is already authenticated");
                        }
                    } else {
                        log.warn("Authorization header is missing or invalid");
                        throw new IllegalStateException("Authorization header is missing or invalid");
                    }
                }else {
                    System.out.println("Error");
                }
                return message;
            }
        });
    }
}
