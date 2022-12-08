package com.seokjin.travelguide.dto.request.trip;

import com.seokjin.travelguide.domain.trip.Trip;
import com.seokjin.travelguide.domain.trip.TripDetail;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripCreateRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String desc;

    @NotBlank
    private String contents;

    @NotBlank
    private String country;

    @NotBlank
    private String city;

    @NotBlank
    @Pattern(regexp = "^(\\+|-)?(?:90(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-8][0-9])(?:(?:\\.[0-9]{1,6})?))$",
            message = "위도는 -90 ~ 90 사이의 값만 허용됩니다.")
    private String latitude;

    @NotBlank
    @Pattern(regexp = "^(\\+|-)?(?:180(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:\\.[0-9]{1,6})?))$",
            message = "경도는 -180 ~ 180 사이의 값만 허용됩니다.")
    private String longitude;

    public Trip toEntity(String author) {
        TripDetail tripDetail = TripDetail.builder()
                .contents(contents)
                .country(country)
                .city(city)
                .latitude(latitude)
                .longitude(longitude)
                .build();

        return Trip.builder()
                .title(title)
                .desc(desc)
                .author(author)
                .tripDetail(tripDetail)
                .build();
    }
}
