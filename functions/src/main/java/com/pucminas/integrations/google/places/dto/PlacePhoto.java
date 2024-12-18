package com.pucminas.integrations.google.places.dto;

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
public class PlacePhoto implements Serializable {

    @Serial
    private static final long serialVersionUID = 2945617133966090962L;

    private String name;
    private List<Photo> photos = new ArrayList<>();

    public void addPhoto(Photo photo) {
        this.photos.add(photo);
    }
}
