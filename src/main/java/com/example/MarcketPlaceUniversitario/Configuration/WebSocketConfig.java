package com.example.MarcketPlaceUniversitario.Configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Canal interno donde el broker distribuye mensajes.
     * /topic/user/{id}  →  el dispositivo del usuario con ese ID recibe el mensaje.
     * /app              →  prefijo para los @MessageMapping del servidor (si los hubiera).
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    /**
     * Punto de entrada WebSocket del backend.
     * Android conecta a  wss://marketplace-final-ncu7.onrender.com/ws
     * (sin SockJS — Android usa WebSocket nativo).
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*");
    }
}
