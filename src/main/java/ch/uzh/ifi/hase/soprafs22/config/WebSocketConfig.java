package ch.uzh.ifi.hase.soprafs22.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.Base64;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final AuthenticationManager authenticationManager;


    public WebSocketConfig(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

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

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    Map<String, List<String>> nativeHeaders = (Map<String, List<String>>) message.getHeaders().get(SimpMessageHeaderAccessor.NATIVE_HEADERS);
                    String authToken = nativeHeaders.get("Authorization").get(0).replace("Basic ", "");
                    String[] decoded = (new String(Base64.getDecoder().decode(authToken))).split(":");
                    UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(decoded[0], decoded[1]);
                    Authentication user = authenticationManager.authenticate(authReq);
                    accessor.setUser(user);
                }
                return message;
            }
        });
    }
}
