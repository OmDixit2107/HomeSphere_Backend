package com.homesphere_backend.repository;

import com.homesphere_backend.entity.ChatMessage;
import com.homesphere_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findBySenderAndRecipientOrRecipientAndSenderOrderByTimestampAsc(
            User sender1, User recipient1, User sender2, User recipient2);

    List<ChatMessage> findByRecipientIsNullOrderByTimestampAsc();

}
