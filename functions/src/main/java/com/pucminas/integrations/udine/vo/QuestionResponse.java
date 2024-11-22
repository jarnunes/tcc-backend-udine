package com.pucminas.integrations.udine.vo;

import java.io.Serializable;

public record QuestionResponse(String response, QuestionFormatType formatType) implements Serializable {
}