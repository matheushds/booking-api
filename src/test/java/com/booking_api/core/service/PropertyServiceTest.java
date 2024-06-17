package com.booking_api.core.service;

import com.booking_api.core.domain.Property;
import com.booking_api.core.exception.PropertyNotFoundException;
import com.booking_api.infrastructure.repository.PropertyRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class PropertyServiceTest {

    private static final Long PROPERTY_ID = 1L;

    @Mock
    private PropertyRepository propertyRepository;

    @InjectMocks
    private PropertyService propertyService;

    @Test
    void shouldFindPropertyById() {
        Mockito.when(propertyRepository.findById(PROPERTY_ID)).thenReturn(java.util.Optional.of(getMockedProperty()));

        propertyService.findPropertyById(PROPERTY_ID);

        Mockito.verify(propertyRepository, Mockito.times(1)).findById(PROPERTY_ID);
    }

    @Test
    void shouldThrowExceptionWhenPropertyNotFound() {
        Mockito.when(propertyRepository.findById(PROPERTY_ID)).thenReturn(java.util.Optional.empty());

        assertThrows(PropertyNotFoundException.class, () -> propertyService.findPropertyById(PROPERTY_ID));
    }

    private Property getMockedProperty() {
        return Property.builder()
                .id(PROPERTY_ID)
                .propertyName("Property Name")
                .propertyLocation("Property Location")
                .build();
    }
}