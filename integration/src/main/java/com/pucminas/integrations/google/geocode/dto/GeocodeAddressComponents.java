package com.pucminas.integrations.google.geocode.dto;

import java.util.List;

public record GeocodeAddressComponents(String long_name, String short_name, List<String> types) {
}
