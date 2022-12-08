package com.seokjin.travelguide.service.trip;

import com.seokjin.travelguide.domain.trip.Trip;
import com.seokjin.travelguide.dto.request.trip.TripCreateRequest;
import com.seokjin.travelguide.dto.request.trip.TripSearchRequest;
import com.seokjin.travelguide.dto.response.trip.TripCreateResponse;
import com.seokjin.travelguide.dto.response.trip.TripDetailResponse;
import com.seokjin.travelguide.dto.response.trip.TripPreviewResponse;
import com.seokjin.travelguide.exception.TripNotFoundException;
import com.seokjin.travelguide.repository.trip.TripRepository;
import com.seokjin.travelguide.service.member.MemberService;
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
    private final MemberService memberService;

    @Transactional
    public TripCreateResponse create(TripCreateRequest request, String email) {
        String nickname = memberService.findNicknameByEmail(email);
        Trip trip = request.toEntity(nickname);
        tripRepository.save(trip);
        log.info("{} 계정이 새로운 여행을 생성했습니다. ID={}", email, trip.getId());
        return TripCreateResponse.of(trip);
    }

    @Transactional(readOnly = true)
    public Page<TripPreviewResponse> getPreviews(TripSearchRequest request) {
        return tripRepository.findTripPreviews(request.getPage(), request.getSize());
    }

    @Transactional(readOnly = true)
    public TripDetailResponse getDetail(Long tripId) {
        return tripRepository.findTripDetail(tripId)
                .orElseThrow(() -> new TripNotFoundException("여행 조회 오류: 존재하지 않는 ID"));
    }
}
