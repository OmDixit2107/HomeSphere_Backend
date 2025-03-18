package com.homesphere_backend.service;

import com.homesphere_backend.DTO.ChatMessageDTO;
import com.homesphere_backend.entity.ChatMessage;
import com.homesphere_backend.repository.ChatMessageRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;

    public ChatMessageService(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    public ChatMessageDTO saveMessage(ChatMessageDTO messageDTO) {
        ChatMessage message = ChatMessage.builder()
                .content(messageDTO.getContent())
                .sender(messageDTO.getSender())
                .recipient(messageDTO.getRecipient())
                .type(messageDTO.getType())
                .timestamp(LocalDateTime.now())
                .build();

        ChatMessage savedMessage = chatMessageRepository.save(message);
        return convertToDTO(savedMessage);
    }

    public List<ChatMessageDTO> getPrivateMessages(String sender, String recipient) {
        return chatMessageRepository
                .findBySenderAndRecipientOrRecipientAndSenderOrderByTimestampAsc(sender, recipient, recipient, sender)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ChatMessageDTO convertToDTO(ChatMessage message) {
        return ChatMessageDTO.builder()
                .content(message.getContent())
                .sender(message.getSender())
                .recipient(message.getRecipient())
                .type(message.getType())
                .timestamp(message.getTimestamp().toString())
                .build();
    }
}
