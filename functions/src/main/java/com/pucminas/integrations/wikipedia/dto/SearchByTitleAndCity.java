package com.pucminas.integrations.wikipedia.dto;

import java.io.Serializable;

public record SearchByTitleAndCity(String title, String city) implements Serializable {
}
