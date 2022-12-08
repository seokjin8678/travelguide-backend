package com.seokjin.travelguide.repository.trip;

import com.seokjin.travelguide.domain.trip.Trip;
import com.seokjin.travelguide.repository.support.Querydsl5RepositorySupport;

public class TripRepositoryImpl extends Querydsl5RepositorySupport implements TripRepositoryCustom {

    public TripRepositoryImpl() {
        super(Trip.class);
    }
}
