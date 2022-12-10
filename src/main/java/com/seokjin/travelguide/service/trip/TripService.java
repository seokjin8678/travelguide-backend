package com.seokjin.travelguide.service.trip;

import com.seokjin.travelguide.domain.trip.Trip;
import com.seokjin.travelguide.domain.trip.TripComment;
import com.seokjin.travelguide.dto.request.trip.TripCommentCreateRequest;
import com.seokjin.travelguide.dto.request.trip.TripCommentSearchRequest;
import com.seokjin.travelguide.dto.request.trip.TripCreateRequest;
import com.seokjin.travelguide.dto.request.trip.TripSearchRequest;
import com.seokjin.travelguide.dto.response.trip.TripCommentResponse;
import com.seokjin.travelguide.dto.response.trip.TripCreateResponse;
import com.seokjin.travelguide.dto.response.trip.TripDetailResponse;
import com.seokjin.travelguide.dto.response.trip.TripPreviewResponse;
import com.seokjin.travelguide.exception.TripNotFoundException;
import com.seokjin.travelguide.repository.trip.TripCommentRepository;
import com.seokjin.travelguide.repository.trip.TripRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TripService {
    private final TripRepository tripRepository;
    private final TripCommentRepository tripCommentRepository;

    @Transactional
    public TripCreateResponse createTrip(TripCreateRequest request, String nickname) {
        Trip trip = request.toEntity(nickname);
        tripRepository.save(trip);
        return TripCreateResponse.of(trip);
    }

    @Transactional(readOnly = true)
    public Page<TripPreviewResponse> getTripPreviews(TripSearchRequest searchRequest) {
        return tripRepository.findTripPreviews(searchRequest);
    }

    @Transactional(readOnly = true)
    public TripDetailResponse getTripDetail(Long tripId) {
        return tripRepository.findTripDetail(tripId)
                .orElseThrow(() -> new TripNotFoundException("여행 조회 오류: 존재하지 않는 ID"));
    }

    @Transactional(readOnly = true)
    public Page<TripCommentResponse> getComments(Long tripId, TripCommentSearchRequest request) {
        return tripCommentRepository.findCommentsById(tripId, request);
    }

    @Transactional
    public Long createComment(TripCommentCreateRequest request, Long tripId, String nickname) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new TripNotFoundException("여행 조회 오류: 존재하지 않는 ID"));
        TripComment comment = request.toEntity(trip, nickname);
        tripCommentRepository.save(comment);
        return comment.getId();
    }
}
