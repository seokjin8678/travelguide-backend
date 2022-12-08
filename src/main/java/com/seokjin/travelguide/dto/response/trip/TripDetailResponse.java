package com.seokjin.travelguide.dto.response.trip;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripDetailResponse {
    private Long id;
    private String title;
    private double score;
    private String desc;
    private String contents;
    private String country;
    private String city;
    private String author; // nickname
    private String latitude;
    private String longitude;

    @QueryProjection
    public TripDetailResponse(Long id, String title, double score, String desc, String contents, String country,
                              String city,
                              String author, String latitude, String longitude) {
        this.id = id;
        this.title = title;
        this.score = score;
        this.desc = desc;
        this.contents = contents;
        this.country = country;
        this.city = city;
        this.author = author;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
