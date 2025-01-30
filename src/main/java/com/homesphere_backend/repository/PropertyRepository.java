package com.homesphere_backend.repository;

import com.homesphere_backend.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {

    List<Property> findByType(String type);   // Fix for findByType
    List<Property> findByStatus(String status);
    List<Property> findByLocation(String location);
    List<Property> findByUserId(Long userId);
    List<Property> findByPriceBetween(Float minPrice, Float maxPrice);
}
