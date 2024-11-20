package com.pucminas.integrations.wikipedia;


import com.pucminas.integrations.wikipedia.dto.WikipediaResponse;
import com.pucminas.utils.MessageUtils;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

@Component
@CommonsLog
public class WikipediaServiceImpl implements WikipediaService {

    private final WikipediaProperties properties;

    @Autowired
    public WikipediaServiceImpl(WikipediaProperties properties) {
        this.properties = properties;
    }

    @Override
    public String getWikipediaText(String title) {
        try {
            return WebClient.builder().baseUrl(properties.getUrl())
                    .build().get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("action", properties.getAction())
                            .queryParam("prop", properties.getProp())
                            .queryParam("format", properties.getFormat())
                            .queryParam("explaintext", properties.getExplaintext())
                            .queryParam("titles", title)
                            .build())
                    .retrieve()
                    .bodyToMono(WikipediaResponse.class)
                    .map(WikipediaResponse::query)
                    .filter(Objects::nonNull)
                    .map(query -> query.pages().values().iterator().next())
                    .map(page -> MessageUtils.defaultIfEmpty(page.extract(), "wikipedia.title.not.found", title))
                    .block();
        } catch (Exception e) {
            log.error(MessageUtils.get("wikipedia.search.title.error", title), e);
            return MessageUtils.get("wikipedia.search.title.error", title);
        }
    }
}
