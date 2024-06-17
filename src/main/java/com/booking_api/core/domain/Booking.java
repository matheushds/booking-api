package com.booking_api.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Setter
@Getter
@SuperBuilder
@DiscriminatorValue("BOOKING")
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Booking extends ScheduleDates {

    @Embedded
    private Guest guestDetails;

    @Column(name = "is_cancelled")
    private boolean isCancelled;

    public Guest getGuestDetails() {
        return guestDetails;
    }

    public void setGuestDetails(Guest guestDetails) {
        this.guestDetails = guestDetails;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }
}
