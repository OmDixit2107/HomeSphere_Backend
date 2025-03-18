package com.homesphere_backend.repository;

import com.homesphere_backend.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findBySenderAndRecipientOrRecipientAndSenderOrderByTimestampAsc(
            String sender1, String recipient1, String sender2, String recipient2);
}
