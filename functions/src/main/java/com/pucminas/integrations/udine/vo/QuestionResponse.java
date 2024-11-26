package com.pucminas.integrations.udine.vo;

import com.pucminas.integrations.google.places.dto.PlacePhoto;
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
public class QuestionResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = -2178099090696424614L;

    private String response;
    private QuestionFormatType formatType;
    private List<PlacePhoto> placePhotos = new ArrayList<>();
    private Boolean success = true;

}