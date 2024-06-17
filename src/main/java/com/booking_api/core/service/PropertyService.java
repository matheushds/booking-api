package com.booking_api.core.service;

import com.booking_api.core.domain.Property;
import com.booking_api.core.exception.PropertyNotFoundException;
import com.booking_api.infrastructure.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyRepository propertyRepository;


    public Property findPropertyById(Long id) {
        return this.propertyRepository.findById(id).orElseThrow(() -> new PropertyNotFoundException("Property not found"));
    }
}
