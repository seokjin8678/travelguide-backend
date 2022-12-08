package com.seokjin.travelguide.dto.response.trip;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripPreviewResponse {
    private String title;
    private Integer score;
    private String desc;
    private String country;
    private String city;
    private String author; // nickname
}
