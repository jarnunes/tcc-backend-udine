package com.pucminas.integrations.udine.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlaceDetails implements Serializable {

    @Serial
    private static final long serialVersionUID = 3924624425814428956L;
    private String name;
    private String id;
    private String address;
    private Double ranting;
    private List<String> opening_hours;
    private String wikipediaDescription;
    private List<String> locationTypes;
    private Double distance;

}
