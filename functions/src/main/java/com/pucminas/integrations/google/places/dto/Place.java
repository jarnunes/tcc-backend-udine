package com.pucminas.integrations.google.places.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Place implements Serializable {

    @Serial
    private static final long serialVersionUID = 1613117863226915039L;

    private String id;
    private List<String> types = new ArrayList<>();
    private String nationalPhoneNumber;
    private String shortFormattedAddress;
    private String city;
    private Location location;
    private Double rating;
    private String googleMapsUri;
    private String websiteUri;
    private Integer userRatingCount;
    private String iconMaskBaseUri;
    private PlaceText displayName;
    private PlaceText editorialSummary;
    private List<String> weekdayDescriptions = new ArrayList<>();
    private OpeningHours currentOpeningHours;
    private List<Review> reviews = new ArrayList<>();
    private List<PlacePhoto> photos = new ArrayList<>();
    private Double distance;

}