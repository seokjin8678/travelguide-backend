package com.seokjin.travelguide.controller;

import com.seokjin.travelguide.dto.request.trip.TripCreateRequest;
import com.seokjin.travelguide.dto.response.Response;
import com.seokjin.travelguide.dto.response.common.SuccessResponse;
import com.seokjin.travelguide.dto.response.trip.TripCreateResponse;
import com.seokjin.travelguide.service.trip.TripService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/trips")
@RequiredArgsConstructor
public class TripController {
    private final TripService tripService;

    @PostMapping
    public ResponseEntity<Response> createTrip(@RequestBody @Valid TripCreateRequest request,
                                               @AuthenticationPrincipal User user) {
        TripCreateResponse response = tripService.create(request, user.getUsername());
        return ResponseEntity.ok()
                .body(SuccessResponse.builder()
                        .code("200")
                        .message("여행이 생성되었습니다!")
                        .result(response)
                        .build());
    }
}
