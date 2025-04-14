package com.homesphere_backend.repository;

import com.homesphere_backend.entity.Property;
import com.homesphere_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {

    List<Property> findByType(String type);   // Fix for findByType
    List<Property> findByStatus(String status);
    List<Property> findByLocation(String location);
    List<Property> findByUserId(Long userId);
    List<Property> findByPriceBetween(Float minPrice, Float maxPrice);

    @Query(value = """
    SELECT DISTINCT u.*
    FROM user u
    WHERE u.id IN (
        SELECT sender_id FROM chat_messages WHERE recipient_id = :ownerId
        UNION
        SELECT recipient_id FROM chat_messages WHERE sender_id = :ownerId
    )
    """, nativeQuery = true)
    List<User> findUsersWhoChattedWith(@Param("ownerId") Long ownerId);

}
