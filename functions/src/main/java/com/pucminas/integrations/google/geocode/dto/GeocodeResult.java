package com.pucminas.integrations.google.geocode.dto;

import java.util.List;

public record GeocodeResult(String place_id, List<String> types, List<GeocodeAddressComponents> address_components) {
}
