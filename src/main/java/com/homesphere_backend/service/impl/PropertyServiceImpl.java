package com.homesphere_backend.service.impl;

import com.homesphere_backend.entity.Property;
import com.homesphere_backend.repository.PropertyRepository;
import com.homesphere_backend.service.PropertyService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;

    @Override
    public Property createProperty(Property property, MultipartFile imageFile) throws IOException {
        property.setImageName(imageFile.getOriginalFilename());
        property.setImageType(imageFile.getContentType());
        property.setImageData(imageFile.getBytes());
        return propertyRepository.save(property);
    }

    @Override
    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }

    @Override
    public Optional<Property> getPropertyById(Long id) {
        return propertyRepository.findById(id);
    }

    @Override
    public List<Property> getPropertiesByUserId(Long userId) {
        return propertyRepository.findByUserId(userId);
    }

    @Override
    public List<Property> getPropertiesByType(String type) {
        return propertyRepository.findByType(type);
    }

    @Override
    public List<Property> getPropertiesByStatus(String status) {
        return propertyRepository.findByStatus(status);
    }

    @Override
    public List<Property> getPropertiesByLocation(String location) {
        return propertyRepository.findByLocation(location);
    }

    @Override
    public Property updateProperty(Long id, Property updatedProperty) {
        return propertyRepository.findById(id).map(property -> {
            property.setTitle(updatedProperty.getTitle());
            property.setDescription(updatedProperty.getDescription());
            property.setPrice(updatedProperty.getPrice());
            property.setLocation(updatedProperty.getLocation());
            property.setType(updatedProperty.getType());
            property.setStatus(updatedProperty.getStatus());
            property.setEmiAvailable(updatedProperty.getEmiAvailable());
            return propertyRepository.save(property);
        }).orElseThrow(() -> new RuntimeException("Property not found"));
    }

    @Override
    public void deleteProperty(Long id) {
        propertyRepository.deleteById(id);
    }

    @Override
    public boolean isEmiAvailable(Long id) {
        return propertyRepository.findById(id)
                .map(Property::getEmiAvailable)
                .orElse(false);
    }

    @Override
    public List<Property> getPropertiesByPriceRange(Float minPrice, Float maxPrice) {
        return propertyRepository.findByPriceBetween(minPrice, maxPrice);
    }
}
