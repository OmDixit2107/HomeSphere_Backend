package com.homesphere_backend.controller;

import com.homesphere_backend.DTO.ChatMessageDTO;
import com.homesphere_backend.service.ChatMessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;

    public ChatController(SimpMessagingTemplate messagingTemplate, ChatMessageService chatMessageService) {
        this.messagingTemplate = messagingTemplate;
        this.chatMessageService = chatMessageService;
    }

    // PUBLIC CHAT
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessageDTO sendMessage(@Payload ChatMessageDTO chatMessage) {
        return chatMessageService.saveMessage(chatMessage);
    }

    // PRIVATE CHAT
    @MessageMapping("/chat.sendPrivateMessage")
    public void sendPrivateMessage(@Payload ChatMessageDTO chatMessage) {
        ChatMessageDTO savedMessage = chatMessageService.saveMessage(chatMessage);

        // Get the recipient's ID from the DTO
        Long recipientId = chatMessage.getRecipientId();
        if (recipientId != null) {
            // Sending message to a specific user
            messagingTemplate.convertAndSendToUser(
                    recipientId.toString(), "/topic/private", savedMessage);
        }
    }

    // JOIN CHAT
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessageDTO addUser(@Payload ChatMessageDTO chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        // Store the sender ID in the session
        if (chatMessage.getSenderId() != null) {
            headerAccessor.getSessionAttributes().put("userId", chatMessage.getSenderId());
        }
        return chatMessageService.saveMessage(chatMessage);
    }

    // GET PRIVATE MESSAGES BETWEEN USERS
    @GetMapping("/private/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessageDTO>> getPrivateMessages(
            @PathVariable Long senderId, @PathVariable Long recipientId) {
        List<ChatMessageDTO> messages = chatMessageService.getPrivateMessages(senderId, recipientId);
        System.out.println(messages);
        System.out.println(senderId);
        System.out.println(recipientId);
        return ResponseEntity.ok(messages);
    }


    // GET PUBLIC MESSAGES
    @GetMapping("/api/chat/messages/public")
    public List<ChatMessageDTO> getPublicMessages() {
        return chatMessageService.getPublicMessages();
    }
}
