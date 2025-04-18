package com.homesphere_backend.service;

import com.homesphere_backend.entity.Property;
import com.homesphere_backend.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface PropertyService {

    // ✅ Create a new property
    Property createProperty(Property property, MultipartFile imageFile) throws IOException;

    // ✅ Retrieve all properties
    List<Property> getAllProperties();

    // ✅ Retrieve a single property by its ID
    Optional<Property> getPropertyById(Long id);

    // ✅ Retrieve all properties owned by a specific user
    List<Property> getPropertiesByUserId(Long userId);

    // ✅ Retrieve properties by type (e.g., "buy", "rent")
    List<Property> getPropertiesByType(String type);

    // ✅ Retrieve properties by status (e.g., "available", "sold", "rented")
    List<Property> getPropertiesByStatus(String status);

    // ✅ Retrieve properties by location
    List<Property> getPropertiesByLocation(String location);

    // ✅ Update an existing property
    Property updateProperty(Long id, Property updatedProperty);

    // ✅ Delete a property by ID
    void deleteProperty(Long id);

    // ✅ Check if EMI is available for a given property
    boolean isEmiAvailable(Long id);

    public List<User> getChatUsersForOwner(Long ownerId);


    // ✅ Filter properties based on price range
    List<Property> getPropertiesByPriceRange(Float minPrice, Float maxPrice);
}
