package com.homesphere_backend.DTO;

import com.homesphere_backend.entity.ChatMessage.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageDTO {

    private String content;

    // Sender and recipient should be represented as IDs
    private Long senderId;
    private Long recipientId;

    private MessageType type;

    private LocalDateTime timestamp;
}
