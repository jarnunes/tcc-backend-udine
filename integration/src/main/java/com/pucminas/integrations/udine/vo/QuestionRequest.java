package com.pucminas.integrations.udine.vo;

import java.util.List;

public record QuestionRequest(String question, QuestionFormatType formatType, List<String> placesId) {
}