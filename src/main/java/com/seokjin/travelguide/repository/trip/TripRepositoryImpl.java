package com.seokjin.travelguide.repository.trip;

import static com.seokjin.travelguide.domain.trip.QTrip.*;
import static com.seokjin.travelguide.domain.trip.QTripComment.*;
import static com.seokjin.travelguide.domain.trip.QTripDetail.*;

import com.seokjin.travelguide.domain.trip.Trip;
import com.seokjin.travelguide.dto.response.trip.QTripDetailResponse;
import com.seokjin.travelguide.dto.response.trip.QTripPreviewResponse;
import com.seokjin.travelguide.dto.response.trip.TripDetailResponse;
import com.seokjin.travelguide.dto.response.trip.TripPreviewResponse;
import com.seokjin.travelguide.repository.support.Querydsl5RepositorySupport;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class TripRepositoryImpl extends Querydsl5RepositorySupport implements TripRepositoryCustom {

    public TripRepositoryImpl() {
        super(Trip.class);
    }

    @Override
    public Optional<TripDetailResponse> findTripDetail(Long tripId) {
        return Optional.ofNullable(
                select(new QTripDetailResponse(
                        trip.id,
                        trip.title,
                        tripComment.score.avg(),
                        trip.desc,
                        tripDetail.contents,
                        tripDetail.country,
                        tripDetail.city,
                        trip.author,
                        tripDetail.latitude,
                        tripDetail.longitude
                ))
                .from(trip)
                .where(trip.id.eq(tripId))
                .innerJoin(trip.tripDetail, tripDetail)
                .leftJoin(tripComment).on(trip.id.eq(tripComment.trip.id))
                .groupBy(trip.id)
                .fetchOne());
    }

    @Override
    public Page<TripPreviewResponse> findTripPreviews(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        return applyPagination(pageRequest,
                query -> query
                        .select(new QTripPreviewResponse(
                                trip.id,
                                trip.title,
                                tripComment.score.avg(),
                                trip.desc,
                                tripDetail.country,
                                tripDetail.city,
                                trip.author))
                        .from(trip)
                        .innerJoin(trip.tripDetail, tripDetail)
                        .leftJoin(tripComment).on(trip.id.eq(tripComment.trip.id))
                        .groupBy(trip.id),
                query -> query
                        .select(trip.count())
                        .from(trip));
    }
}
