package com.seokjin.travelguide.repository.trip;

import com.seokjin.travelguide.dto.request.trip.TripCommentSearchRequest;
import com.seokjin.travelguide.dto.response.trip.TripCommentResponse;
import org.springframework.data.domain.Page;

public interface TripCommentRepositoryCustom {
    Page<TripCommentResponse> findCommentsById(Long tripId, TripCommentSearchRequest request);

}
