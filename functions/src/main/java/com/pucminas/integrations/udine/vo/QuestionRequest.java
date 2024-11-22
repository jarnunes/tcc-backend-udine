package com.pucminas.integrations.udine.vo;

import java.io.Serializable;
import java.util.List;

public record QuestionRequest (
        String question,
        QuestionFormatType formatType,
        List<String> placesId) implements Serializable {
}