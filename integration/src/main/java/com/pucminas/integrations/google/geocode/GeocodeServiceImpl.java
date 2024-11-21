package com.pucminas.integrations.google.geocode;

import com.pucminas.integrations.google.geocode.dto.GeocodeAddressComponents;
import com.pucminas.integrations.google.geocode.dto.GeocodeResponse;
import com.pucminas.integrations.google.geocode.dto.GeocodeResult;
import com.pucminas.utils.JsonUtils;
import com.pucminas.utils.StrUtils;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collection;

@Component
@CommonsLog
public class GeocodeServiceImpl implements GeocodeService {

    private GeocodeProperties properties;

    @Autowired
    public void setProperties(GeocodeProperties properties) {
        this.properties = properties;
    }

    @Override
    public String getCityName(Double latitude, Double longitude) {
        final GeocodeResponse response = WebClient.builder().baseUrl(properties.getUrl()).build()
                .get()
                .uri(uri -> uri
                        .queryParam("key", properties.getApiKey())
                        .queryParam("latlng", StrUtils.joinObjects(latitude, longitude))
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(GeocodeResponse.class)
                .block();

        return response.results().stream()
                .map(GeocodeResult::address_components)
                .flatMap(Collection::stream)
                .filter(address -> address.types().contains("locality"))
                .findFirst()
                .map(GeocodeAddressComponents::long_name)
                .orElse(null);
    }
}
