package com.seokjin.travelguide.dto.response.trip;

import com.seokjin.travelguide.domain.trip.Trip;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripCreateResponse {
    private Long tripId;

    public TripCreateResponse(Long tripId) {
        this.tripId = tripId;
    }

    public static TripCreateResponse of(Trip trip) {
        return new TripCreateResponse(trip.getId());
    }
}
