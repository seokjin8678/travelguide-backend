package com.seokjin.travelguide.repository.trip;

import com.seokjin.travelguide.domain.trip.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripRepository extends JpaRepository<Trip, Long>, TripRepositoryCustom {
}
