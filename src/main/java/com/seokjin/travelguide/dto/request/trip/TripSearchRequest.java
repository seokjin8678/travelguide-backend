package com.seokjin.travelguide.dto.request.trip;

import static java.lang.Math.max;
import static java.lang.Math.min;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripSearchRequest {

    private static final int MAX_SIZE = 1000;

    private Integer page = 1;

    private Integer size = 8;

    public long getOffset() {
        return (long) (max(1, page) - 1) * min(size, MAX_SIZE);
    }
}
