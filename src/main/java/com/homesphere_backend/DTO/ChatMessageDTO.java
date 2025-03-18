package com.homesphere_backend.DTO;

import com.homesphere_backend.entity.ChatMessage.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageDTO {
    private String content;
    private String sender;
    private String recipient;
    private MessageType type;
    private String timestamp;
}