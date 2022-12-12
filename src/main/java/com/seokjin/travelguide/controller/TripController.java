package com.seokjin.travelguide.controller;

import com.seokjin.travelguide.dto.request.trip.TripCommentCreateRequest;
import com.seokjin.travelguide.dto.request.trip.TripCommentSearchRequest;
import com.seokjin.travelguide.dto.response.common.SimpleResponse;
import com.seokjin.travelguide.dto.response.trip.TripCommentResponse;
import com.seokjin.travelguide.service.auth.CustomUser;
import com.seokjin.travelguide.dto.request.trip.TripCreateRequest;
import com.seokjin.travelguide.dto.request.trip.TripSearchRequest;
import com.seokjin.travelguide.dto.response.Response;
import com.seokjin.travelguide.dto.response.common.SuccessResponse;
import com.seokjin.travelguide.dto.response.trip.TripCreateResponse;
import com.seokjin.travelguide.dto.response.trip.TripDetailResponse;
import com.seokjin.travelguide.dto.response.trip.TripPreviewResponse;
import com.seokjin.travelguide.service.trip.TripService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/trips")
@RequiredArgsConstructor
public class TripController {
    private final TripService tripService;

    @PostMapping
    public ResponseEntity<Response> createTrip(@RequestBody @Valid TripCreateRequest request,
                                               @AuthenticationPrincipal CustomUser user) {
        TripCreateResponse response = tripService.createTrip(request, user.getNickname());
        log.info("{} 계정이 새로운 여행을 생성했습니다. ID={}", user.getUsername(), response.getTripId());
        return ResponseEntity.ok()
                .body(SuccessResponse.builder()
                        .code("200")
                        .message("여행이 생성되었습니다!")
                        .result(response)
                        .build());
    }

    @GetMapping
    public Page<TripPreviewResponse> getTripPreviews(@ModelAttribute @Valid TripSearchRequest request) {
        return tripService.getTripPreviews(request);
    }

    @GetMapping("/{tripId}")
    public ResponseEntity<Response> getTripDetail(@PathVariable Long tripId) {
        TripDetailResponse response = tripService.getTripDetail(tripId);
        return ResponseEntity.ok()
                .body(SuccessResponse.builder()
                        .code("200")
                        .message("여행 ID=" + tripId + " 의 결과입니다.")
                        .result(response)
                        .build());
    }

    @GetMapping("/{tripId}/comments")
    public Page<TripCommentResponse> getTripComments(@PathVariable Long tripId,
                                                     @ModelAttribute @Valid TripCommentSearchRequest request) {
        return tripService.getComments(tripId, request);
    }

    @PostMapping("/{tripId}/comments")
    public ResponseEntity<Response> createTripComment(@PathVariable Long tripId, @RequestBody @Valid
    TripCommentCreateRequest request, @AuthenticationPrincipal CustomUser user) {
        Long commentId = tripService.createComment(request, tripId, user.getNickname());
        log.info("{} 계정이 코멘트를 작성했습니다. TripId={}, CommentId={}", user.getUsername(), tripId, commentId);
        return ResponseEntity.ok()
                .body(SimpleResponse.of("200", "댓글이 작성되었습니다."));
    }
}
