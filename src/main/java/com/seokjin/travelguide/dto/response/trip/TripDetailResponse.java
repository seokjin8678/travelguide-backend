package com.seokjin.travelguide.dto.response.trip;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripDetailResponse {
    private String title;
    private String score;
    private String desc;
    private String contents;
    private String country;
    private String city;
    private String author; // nickname
    private String latitude;
    private String longitude;
}
