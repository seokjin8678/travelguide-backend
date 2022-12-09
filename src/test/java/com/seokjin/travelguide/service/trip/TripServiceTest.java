package com.seokjin.travelguide.service.trip;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import com.seokjin.travelguide.domain.trip.Trip;
import com.seokjin.travelguide.dto.request.trip.TripCreateRequest;
import com.seokjin.travelguide.dto.response.trip.TripCreateResponse;
import com.seokjin.travelguide.repository.trip.TripRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TripServiceTest {

    @Mock
    TripRepository tripRepository;

    @InjectMocks
    TripService tripService;

    @Test
    @DisplayName("여행이 정상적으로 생성되어야 한다.")
    void tripCouldBeSave() {
        // given
        TripCreateRequest request = new TripCreateRequest();
        request.setTitle("test title");
        request.setContents("test contents");
        request.setDesc("test desc");
        request.setCountry("south korea");
        request.setCity("seoul");
        request.setLatitude("37.532600");
        request.setLongitude("127.024612");
        Trip trip = request.toEntity("nickname");
        doReturn(trip).when(tripRepository)
                .save(any(Trip.class));

        // when
        TripCreateResponse response = tripService.create(request, "nickname");

        // then
        assertThat(response).isNotNull();
    }
}