package ch.uzh.ifi.hase.soprafs22.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // These are endpoints the client can subscribe to.
        config.enableSimpleBroker("/client");

        // Message received with one of those below destinationPrefixes
        // will be automatically routed to controllers @MessageMapping
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // handshake endpoint
        registry.addEndpoint("/websocket");
        registry.addEndpoint("/websocket").setAllowedOrigins("http://localhost:3000", "https://sopra-fs22-group-29-client.herokuapp.com").withSockJS();
    }
}
