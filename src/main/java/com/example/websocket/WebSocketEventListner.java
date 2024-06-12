package com.example.websocket;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class WebSocketEventListner {
    private final SimpMessageSendingOperations messageTemplate;
    @EventListener
    public void handleWebSocketDisconnectListner(SessionDisconnectEvent event){
       StompHeaderAccessor headerAccessor =StompHeaderAccessor.wrap(event.getMessage());
       String username=headerAccessor.getSessionAttributes().get("username").toString();
       if(username != null){
         log.info("User disconnected: {}", username);
         var chatMessage=ChatMessage.builder().sender(username).type(MessageType.LEAVE).build();
         messageTemplate.convertAndSend("/topic/public",chatMessage);
       }
    }
}
