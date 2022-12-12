package com.seokjin.travelguide.dto.request.trip;

import javax.validation.constraints.Positive;
import lombok.Setter;

@Setter
public class TripCommentSearchRequest {

    @Positive
    private Integer page = 1;

    @Positive
    private Integer size = 10;

    public Integer getPage() {
        return page - 1;
    }

    public Integer getSize() {
        return size;
    }
}
