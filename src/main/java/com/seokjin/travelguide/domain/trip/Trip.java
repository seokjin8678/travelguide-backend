package com.seokjin.travelguide.domain.trip;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String desc;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "trip_detail_id")
    private TripDetail tripDetail;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String city;

    @Builder
    public Trip(String title, String desc, TripDetail tripDetail, String author, String country, String city) {
        this.title = title;
        this.desc = desc;
        this.tripDetail = tripDetail;
        this.author = author;
        this.country = country;
        this.city = city;
    }
}
