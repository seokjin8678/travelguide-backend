package com.seokjin.travelguide.dto.request.trip;

import com.seokjin.travelguide.domain.trip.Trip;
import com.seokjin.travelguide.domain.trip.TripComment;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripCommentCreateRequest {

    @NotBlank
    private String comment;

    @PositiveOrZero
    @Max(value = 5)
    private int score;

    public TripComment toEntity(Trip trip, String author) {
        return TripComment.builder()
                .comment(comment)
                .score(score)
                .author(author)
                .trip(trip)
                .build();
    }
}
