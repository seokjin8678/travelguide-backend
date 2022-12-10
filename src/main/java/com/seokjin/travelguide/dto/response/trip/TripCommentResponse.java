package com.seokjin.travelguide.dto.response.trip;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripCommentResponse {
    private Long id;
    private String comment;
    private int score;
    private String author;

    @Builder
    @QueryProjection
    public TripCommentResponse(Long id, String comment, int score, String author) {
        this.id = id;
        this.comment = comment;
        this.score = score;
        this.author = author;
    }
}
