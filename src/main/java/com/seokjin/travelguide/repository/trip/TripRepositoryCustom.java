package com.seokjin.travelguide.repository.trip;

import com.seokjin.travelguide.dto.response.trip.TripDetailResponse;
import com.seokjin.travelguide.dto.response.trip.TripPreviewResponse;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface TripRepositoryCustom {
    Optional<TripDetailResponse> findTripDetail(Long tripId);

    Page<TripPreviewResponse> findTripPreviews(Integer page, Integer size);
}
