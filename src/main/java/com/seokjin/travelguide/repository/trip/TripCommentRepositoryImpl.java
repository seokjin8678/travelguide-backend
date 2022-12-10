package com.seokjin.travelguide.repository.trip;

import static com.seokjin.travelguide.domain.trip.QTripComment.*;

import com.seokjin.travelguide.domain.trip.TripComment;
import com.seokjin.travelguide.dto.request.trip.TripCommentSearchRequest;
import com.seokjin.travelguide.dto.response.trip.QTripCommentResponse;
import com.seokjin.travelguide.dto.response.trip.TripCommentResponse;
import com.seokjin.travelguide.repository.support.Querydsl5RepositorySupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class TripCommentRepositoryImpl extends Querydsl5RepositorySupport implements TripCommentRepositoryCustom {
    public TripCommentRepositoryImpl() {
        super(TripComment.class);
    }

    @Override
    public Page<TripCommentResponse> findCommentsById(Long tripId, TripCommentSearchRequest request) {
        PageRequest pageRequest = PageRequest.of(request.getPage(), request.getSize(),
                Sort.by("id").descending());

        return applyPagination(pageRequest,
                query -> query
                        .select(new QTripCommentResponse(
                                tripComment.id,
                                tripComment.comment,
                                tripComment.score,
                                tripComment.author
                        ))
                        .from(tripComment)
                        .where(tripComment.trip.id.eq(tripId)),
                query -> query
                        .select(tripComment.count())
                        .from(tripComment)
                        .where(tripComment.trip.id.eq(tripId))
        );
    }

}
