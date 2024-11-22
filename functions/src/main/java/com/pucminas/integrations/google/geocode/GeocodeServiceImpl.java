package com.pucminas.integrations.google.geocode;

import com.pucminas.commons.utils.StrUtils;
import com.pucminas.integrations.ServiceBase;
import com.pucminas.integrations.google.geocode.dto.GeocodeAddressComponents;
import com.pucminas.integrations.google.geocode.dto.GeocodeResponse;
import com.pucminas.integrations.google.geocode.dto.GeocodeResult;
import lombok.*;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;

@Component
@CommonsLog
public class GeocodeServiceImpl extends ServiceBase implements GeocodeService {

    private GeocodeProperties properties;

    @Autowired
    public void setProperties(GeocodeProperties properties) {
        this.properties = properties;
    }

    @Override
    protected String serviceNameKey() {
        return "geocode.service.name";
    }

    @Override
    public String getCityName(Double latitude, Double longitude) {
        final GeocodeParams params = new GeocodeParams(latitude, longitude);
        return cacheService.getCachedValueOrNew(getClass(), "CACHE_KEY_GET_CITY_NAME", params,
            this::geCityNameByLatLgn);
    }

    private String geCityNameByLatLgn(GeocodeParams params) {
        return processWithAttempts(3, params, () -> {
            final GeocodeResponse response = WebClient.builder().baseUrl(properties.getUrl()).build()
                    .get()
                    .uri(uri -> uri
                            .queryParam("key", properties.getApiKey())
                            .queryParam("latlng", StrUtils.joinObjects(params.getLatitude(), params.getLongitude()))
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
        });
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    private static class GeocodeParams implements Serializable {

        @Serial
        private static final long serialVersionUID = 1743479381381523373L;

        private  Double latitude;
        private  Double longitude;

    }
}
