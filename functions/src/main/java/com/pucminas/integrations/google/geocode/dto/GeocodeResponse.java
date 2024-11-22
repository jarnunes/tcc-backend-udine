package com.pucminas.integrations.google.geocode.dto;

import java.util.List;

public record GeocodeResponse(List<GeocodeResult> results) {
}
