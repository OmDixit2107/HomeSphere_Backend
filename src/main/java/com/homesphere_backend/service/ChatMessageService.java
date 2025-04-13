package com.homesphere_backend.service;

import com.homesphere_backend.DTO.ChatMessageDTO;
import com.homesphere_backend.entity.ChatMessage;
import com.homesphere_backend.entity.User;
import com.homesphere_backend.repository.ChatMessageRepository;
import com.homesphere_backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    public ChatMessageService(ChatMessageRepository chatMessageRepository, UserRepository userRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ChatMessageDTO saveMessage(ChatMessageDTO messageDTO) {
        User sender = null;
        User recipient = null;

        // Fetch sender from database
        if (messageDTO.getSenderId() != null) {
            sender = userRepository.findById(messageDTO.getSenderId())
                    .orElseThrow(() -> new IllegalArgumentException("Sender not found with ID: " + messageDTO.getSenderId()));
        }

        // Handle public and private messages properly
        if (messageDTO.getRecipientId() != null && messageDTO.getRecipientId() != 0) {
            recipient = userRepository.findById(messageDTO.getRecipientId())
                    .orElseThrow(() -> new IllegalArgumentException("Recipient not found with ID: " + messageDTO.getRecipientId()));
        }

        ChatMessage message = ChatMessage.builder()
                .content(messageDTO.getContent())
                .sender(sender)
                .recipient(recipient) // Set to null for public messages
                .type(messageDTO.getType())
                .timestamp(LocalDateTime.now())
                .build();

        // Save the message
        ChatMessage savedMessage = chatMessageRepository.save(message);
        return convertToDTO(savedMessage);
    }

    @Transactional(readOnly = true)
    public List<ChatMessageDTO> getPrivateMessages(Long senderId, Long recipientId) {
        // Fetch sender and recipient from the database
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("Sender not found with ID: " + senderId));
        User recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new IllegalArgumentException("Recipient not found with ID: " + recipientId));

        return chatMessageRepository
                .findBySenderAndRecipientOrRecipientAndSenderOrderByTimestampAsc(sender, recipient, recipient, sender)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ChatMessageDTO> getPublicMessages() {
        return chatMessageRepository
                .findByRecipientIsNullOrderByTimestampAsc()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ChatMessageDTO convertToDTO(ChatMessage message) {
        return ChatMessageDTO.builder()
                .content(message.getContent())
                .senderId(message.getSender() != null ? message.getSender().getId() : null)
                .recipientId(message.getRecipient() != null ? message.getRecipient().getId() : null)
                .type(message.getType())
                .timestamp(message.getTimestamp())
                .build();
    }
}
