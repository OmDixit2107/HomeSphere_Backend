package com.homesphere_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homesphere_backend.entity.Property;
import com.homesphere_backend.service.PropertyService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("api/properties")
public class PropertyController {

    private final PropertyService propertyService;

    // Create a new property
    @PostMapping("/create")
    public ResponseEntity<?> createProperty(
            @RequestPart("property") String propertyJson,
            @RequestParam("imageFile") MultipartFile imageFile) {

        try {
            // Deserialize the property object from the JSON string
            Property property = new ObjectMapper().readValue(propertyJson, Property.class);

            // Call the service method to handle saving the property with the image
            Property savedProperty = propertyService.createProperty(property, imageFile);

            // Return the saved property in the response
            return ResponseEntity.ok(savedProperty);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get all properties
    @GetMapping
    public ResponseEntity<List<Property>> getAllProperties() {
        List<Property> properties = propertyService.getAllProperties();
        return ResponseEntity.ok(properties);
    }

    // Get property by ID
    @GetMapping("/{id}")
    public ResponseEntity<Property> getPropertyById(@PathVariable Long id) {
        Optional<Property> property = propertyService.getPropertyById(id);
        return property.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Get properties by type (e.g., "buy" or "rent")
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Property>> getPropertiesByType(@PathVariable String type) {
        List<Property> properties = propertyService.getPropertiesByType(type);
        return ResponseEntity.ok(properties);
    }

    // Get properties by status (e.g., "available", "sold", "rented")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Property>> getPropertiesByStatus(@PathVariable String status) {
        List<Property> properties = propertyService.getPropertiesByStatus(status);
        return ResponseEntity.ok(properties);
    }

    // Get properties by location
    @GetMapping("/location/{location}")
    public ResponseEntity<List<Property>> getPropertiesByLocation(@PathVariable String location) {
        List<Property> properties = propertyService.getPropertiesByLocation(location);
        return ResponseEntity.ok(properties);
    }

    // Update a property
    @PutMapping("/{id}")
    public ResponseEntity<Property> updateProperty(@PathVariable Long id, @RequestBody Property updatedProperty) {
        Property updated = propertyService.updateProperty(id, updatedProperty);
        return ResponseEntity.ok(updated);
    }

    // Delete a property
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProperty(@PathVariable Long id) {
        propertyService.deleteProperty(id);
        return ResponseEntity.noContent().build();
    }

    // Check if EMI is available for a property
    @GetMapping("/{id}/emi")
    public ResponseEntity<Boolean> isEmiAvailable(@PathVariable Long id) {
        boolean emiAvailable = propertyService.isEmiAvailable(id);
        return ResponseEntity.ok(emiAvailable);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<Property>> getPropertiesByUserId(@PathVariable Long id){
        List<Property> properties=propertyService.getPropertiesByUserId(id);
        return ResponseEntity.ok(properties);
    }

    @GetMapping("/user/image/{id}")
    public ResponseEntity<byte[]>getImageByPropertyId(@PathVariable Long id){
        Optional<Property> property=propertyService.getPropertyById(id);
        byte[]imageFile=property.get().getImageData();

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(property.get().getImageType()))
                .body(imageFile);
    }
}
