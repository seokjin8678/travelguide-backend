package com.seokjin.travelguide.repository.trip;

import static org.assertj.core.api.Assertions.*;

import com.seokjin.travelguide.domain.trip.Trip;
import com.seokjin.travelguide.domain.trip.TripComment;
import com.seokjin.travelguide.domain.trip.TripDetail;
import com.seokjin.travelguide.dto.request.trip.TripSearchRequest;
import com.seokjin.travelguide.dto.response.trip.TripDetailResponse;
import com.seokjin.travelguide.dto.response.trip.TripPreviewResponse;
import java.util.List;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;

@DataJpaTest
class TripRepositoryTest {

    @Autowired
    TripRepository tripRepository;

    @Autowired
    TripCommentRepository tripCommentRepository;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("데이터베이스에 여행이 정상적으로 저장되어야 한다.")
    void saveTripToDatabaseCouldBeSuccess() {
        // given
        Trip trip = trip();
        tripRepository.save(trip);

        // when
        List<Trip> trips = tripRepository.findAll();

        // then
        assertThat(trips.size())
                .isEqualTo(1L);
    }

    @Test
    @DisplayName("데이터베이스에 여행이 정상적으로 조회되어야 한다.")
    void findTripsToDatabaseCouldBeSuccess() {
        // given
        Trip trip = trip();

        // when
        tripRepository.save(trip);
        em.clear();
        Trip findTrip = tripRepository.findById(trip.getId()).get();

        // then
        assertThat(findTrip.getId())
                .isEqualTo(trip.getId());
        assertThat(findTrip.getTitle())
                .isEqualTo(trip.getTitle());
        assertThat(findTrip.getTripDetail().getContents())
                .isEqualTo(trip.getTripDetail().getContents());
    }

    @Test
    @DisplayName("TripSearchRequest가 주어졌을 때 정상적으로 TripPreviewResponse가 조회되어야 한다.")
    void findTripPreviewResponseByTripSearchRequestCouldBeSuccess() {
        // given
        IntStream.rangeClosed(1, 10).forEach(i -> tripRepository.save(trip()));
        TripSearchRequest searchRequest = new TripSearchRequest();

        // when
        Page<TripPreviewResponse> tripPreviews = tripRepository.findTripPreviews(searchRequest);

        // then
        assertThat(tripPreviews.getTotalPages())
                .isEqualTo(2);
        assertThat(tripPreviews.getTotalElements())
                .isEqualTo(10);
        assertThat(tripPreviews.getSize())
                .isEqualTo(8);
    }

    @Test
    @DisplayName("TripId가 주어졌을 때 정상적으로 TripDetailResponse가 조회되어야 한다.")
    void findTripDetailResponseByTripIdCouldBeSuccess() {
        // given
        Trip trip = trip();
        tripRepository.save(trip);

        // when
        TripDetailResponse tripDetailResponse = tripRepository.findTripDetail(trip.getId()).get();

        // then
        assertThat(tripDetailResponse.getTitle())
                .isEqualTo(trip.getTitle());
        assertThat(tripDetailResponse.getContents())
                .isEqualTo(trip.getTripDetail().getContents());
        assertThat(tripDetailResponse.getLatitude())
                .isEqualTo(trip.getTripDetail().getLatitude());
        assertThat(tripDetailResponse.getLongitude())
                .isEqualTo(trip.getTripDetail().getLongitude());
    }

    @Test
    @DisplayName("TripPreviewResponse를 조회할 때 평점이 정확하게 계산되어야 한다.")
    void findTripPreviewResponseCouldBeScoreAccuratelyCalculate() {
        // given
        Trip trip1 = trip();
        Trip trip2 = trip();
        tripCommentRepository.save(tripComment(trip1, 2));
        tripCommentRepository.save(tripComment(trip1, 3));
        tripCommentRepository.save(tripComment(trip1, 5));
        tripCommentRepository.save(tripComment(trip2, 5));
        tripCommentRepository.save(tripComment(trip2, 5));
        tripCommentRepository.save(tripComment(trip2, 5));
        tripRepository.save(trip1);
        tripRepository.save(trip2);
        TripSearchRequest searchRequest = new TripSearchRequest();

        // when
        Page<TripPreviewResponse> tripPreviews = tripRepository.findTripPreviews(searchRequest);

        // then
        assertThat(tripPreviews.getContent().get(0).getScore())
                .isEqualTo(5);
        assertThat(tripPreviews.getContent().get(1).getScore())
                .isEqualTo(10.0 / 3);
    }

    @Test
    @DisplayName("TripDetailResponse를 조회할 때 평점이 정확하게 계산되어야 한다.")
    void findTripDetailResponseCouldBeScoreAccuratelyCalculate() {
        // given
        Trip trip = trip();
        tripCommentRepository.save(tripComment(trip, 2));
        tripCommentRepository.save(tripComment(trip, 3));
        tripCommentRepository.save(tripComment(trip, 5));
        tripRepository.save(trip);

        // when
        TripDetailResponse tripDetailResponse = tripRepository.findTripDetail(trip.getId()).get();

        // then
        assertThat(tripDetailResponse.getScore())
                .isEqualTo(10.0 / 3);
    }

    private Trip trip() {
        TripDetail tripDetail = TripDetail.builder()
                .contents("내용")
                .latitude("12")
                .longitude("21")
                .build();

        return Trip.builder()
                .title("타이틀")
                .author("작성자")
                .desc("설명")
                .country("대한민국")
                .city("서울")
                .tripDetail(tripDetail)
                .build();
    }

    private TripComment tripComment(Trip trip, int score) {
        return TripComment.builder()
                .comment("comment")
                .trip(trip)
                .score(score)
                .author("author")
                .build();
    }
}