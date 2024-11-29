package com.pucminas.integrations.wikipedia;

import com.pucminas.commons.utils.MessageUtils;
import com.pucminas.commons.utils.StrUtils;
import com.pucminas.integrations.ServiceBase;
import com.pucminas.integrations.wikipedia.dto.QueryLikeResponse;
import com.pucminas.integrations.wikipedia.dto.SearchLike;
import com.pucminas.integrations.wikipedia.dto.WikipediaQueryLikeResponse;
import com.pucminas.integrations.wikipedia.dto.WikipediaResponse;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Objects;

@Component
@CommonsLog
public class WikipediaServiceImpl extends ServiceBase implements WikipediaService {

    private final WikipediaProperties properties;

    @Autowired
    public WikipediaServiceImpl(WikipediaProperties properties) {
        this.properties = properties;
    }

    @Override
    protected String serviceNameKey() {
        return "wikipedia.service.name";
    }

    @Override
    public String getWikipediaText(String title) {
        return cacheService.getCachedValueOrNew(getClass(), "CACHE_KEY_GET_WIKIPEDIA_TITLE", title,
            this::searchOnWikipediaByTitle);
    }

    private String searchOnWikipediaByTitle(String title) {
        return processWithAttempts(3, title, () -> WebClient.builder().baseUrl(properties.getUrl())
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
                .map(StrUtils::removeMarkdownFormatting)
                .mapNotNull(text -> StringUtils.truncate(text, properties.getTextLength()))
                .block());
    }

    @Override
    public List<SearchLike> getNearestWikipediaTitles(String name) {
        cacheService.removeCacheItem(getClass(), "CACHE_KEY_NEAREST_WIKIPEDIA_TITLES", name);
        return cacheService.getCachedValueOrNew(getClass(), "CACHE_KEY_NEAREST_WIKIPEDIA_TITLES", name, t ->
               processWithAttempts(3, name, () ->
                WebClient.builder().baseUrl(properties.getUrl())
                        .build().get()
                        .uri(uriBuilder -> uriBuilder
                                .queryParam("action", properties.getAction())
                                .queryParam("prop", properties.getProp())
                                .queryParam("format", properties.getFormat())
                                .queryParam("explaintext", properties.getExplaintext())
                                .queryParam("list", "search")
                                .queryParam("srsearch", name)
                                .build())
                        .retrieve()
                        .bodyToMono(WikipediaQueryLikeResponse.class)
                        .mapNotNull(WikipediaQueryLikeResponse::query)
                        .mapNotNull(QueryLikeResponse::search)
                        .block()));
    }

}
