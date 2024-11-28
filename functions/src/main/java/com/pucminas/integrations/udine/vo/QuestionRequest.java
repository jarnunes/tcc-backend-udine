package com.pucminas.integrations.udine.vo;

import com.pucminas.integrations.google.places.dto.Location;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class QuestionRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 925668793160077316L;

    private String question;
    private QuestionFormatType formatType;
    private Location location;
    private List<String> placesId;

}