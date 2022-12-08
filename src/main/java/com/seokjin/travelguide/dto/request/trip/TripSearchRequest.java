package com.seokjin.travelguide.dto.request.trip;

import lombok.Setter;

@Setter
public class TripSearchRequest {

    private Integer page = 1;

    private Integer size = 8;

    public Integer getPage() {
        return page - 1;
    }

    public Integer getSize() {
        return size;
    }
}
