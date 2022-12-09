package com.seokjin.travelguide.dto.response.trip;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripPreviewResponse {
    private Long id;
    private String title;
    private double score;
    private String desc;
    private String country;
    private String city;
    private String author; // nickname

    @Builder
    @QueryProjection
    public TripPreviewResponse(Long id, String title, double score, String desc, String country, String city,
                               String author) {
        this.id = id;
        this.title = title;
        this.score = score;
        this.desc = desc;
        this.country = country;
        this.city = city;
        this.author = author;
    }
}
