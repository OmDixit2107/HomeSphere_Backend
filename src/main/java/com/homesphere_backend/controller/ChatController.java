package com.homesphere_backend.controller;

import com.homesphere_backend.DTO.ChatMessageDTO;
import com.homesphere_backend.service.ChatMessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
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
    public void sendMessage(@Payload ChatMessageDTO chatMessage) {
        ChatMessageDTO savedMessage = chatMessageService.saveMessage(chatMessage);
        messagingTemplate.convertAndSend("/topic/public", savedMessage);
    }

    // PRIVATE CHAT
    @MessageMapping("/chat.sendPrivateMessage")
    public void sendPrivateMessage(@Payload ChatMessageDTO chatMessage) {
        ChatMessageDTO savedMessage = chatMessageService.saveMessage(chatMessage);

        // Sending message to a specific user ("/user/{recipient}/queue/messages")
        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipient(), "/queue/messages", savedMessage);
    }

    // GET PRIVATE MESSAGES BETWEEN USERS
    @GetMapping("/api/chat/messages/private")
    public List<ChatMessageDTO> getPrivateMessages(
            @RequestParam String sender,
            @RequestParam String recipient) {
        return chatMessageService.getPrivateMessages(sender, recipient);
    }
}
