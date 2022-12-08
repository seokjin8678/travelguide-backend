package com.seokjin.travelguide.service.trip;

import com.seokjin.travelguide.domain.trip.Trip;
import com.seokjin.travelguide.dto.request.trip.TripCreateRequest;
import com.seokjin.travelguide.dto.response.trip.TripCreateResponse;
import com.seokjin.travelguide.repository.trip.TripRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TripService {
    private final TripRepository tripRepository;

    @Transactional
    public TripCreateResponse create(TripCreateRequest request, String email) {
        Trip trip = request.toEntity(email);
        tripRepository.save(trip);
        log.info("{} 계정이 새로운 여행을 생성했습니다. ID={}", email, trip.getId());
        return TripCreateResponse.of(trip);
    }
}
