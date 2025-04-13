package com.homesphere_backend.config;

import com.homesphere_backend.DTO.ChatMessageDTO;
import com.homesphere_backend.entity.ChatMessage.MessageType;
import com.homesphere_backend.service.ChatMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Objects;

@Component
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);
    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatMessageService chatMessageService;

    public WebSocketEventListener(SimpMessageSendingOperations messagingTemplate,
            ChatMessageService chatMessageService) {
        this.messagingTemplate = messagingTemplate;
        this.chatMessageService = chatMessageService;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        logger.info("New WebSocket connection established.");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        Long userId = (Long) headerAccessor.getSessionAttributes().get("userId");

        if (Objects.nonNull(userId)) {
            logger.info("User Disconnected: User ID " + userId);

            // Create a leave message
            ChatMessageDTO chatMessage = ChatMessageDTO.builder()
                    .type(MessageType.LEAVE)
                    .senderId(userId)
                    .build();

            // Save the leave message
            ChatMessageDTO savedMessage = chatMessageService.saveMessage(chatMessage);

            // Broadcast to all users
            messagingTemplate.convertAndSend("/topic/public", savedMessage);
        }
    }
}
