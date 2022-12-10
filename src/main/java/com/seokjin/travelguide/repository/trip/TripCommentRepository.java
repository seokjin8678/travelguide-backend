package com.seokjin.travelguide.repository.trip;

import com.seokjin.travelguide.domain.trip.TripComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripCommentRepository extends JpaRepository<TripComment, Long>, TripCommentRepositoryCustom {
}
