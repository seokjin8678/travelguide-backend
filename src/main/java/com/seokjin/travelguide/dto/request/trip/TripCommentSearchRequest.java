package com.seokjin.travelguide.dto.request.trip;

import lombok.Setter;

@Setter
public class TripCommentSearchRequest {

    private Integer page = 1;

    private Integer size = 10;

    public Integer getPage() {
        return page - 1;
    }

    public Integer getSize() {
        return size;
    }
}
