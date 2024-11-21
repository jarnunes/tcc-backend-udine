package com.pucminas.integrations.wikipedia;

import com.pucminas.integrations.CacheService;
import com.pucminas.integrations.openai.OpenAiService;
import com.pucminas.integrations.wikipedia.dto.QueryLikeResponse;
import com.pucminas.integrations.wikipedia.dto.SearchLike;
import com.pucminas.integrations.wikipedia.dto.WikipediaQueryLikeResponse;
import com.pucminas.integrations.wikipedia.dto.WikipediaResponse;
import com.pucminas.utils.MessageUtils;
import com.pucminas.utils.StrUtils;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Objects;

@Component
@CommonsLog
public class WikipediaServiceImpl implements WikipediaService {

    private final WikipediaProperties properties;
    private OpenAiService openAiService;
    private CacheService cacheService;

    @Autowired
    public WikipediaServiceImpl(WikipediaProperties properties) {
        this.properties = properties;
    }

    @Autowired
    public void setOpenAiService(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    @Autowired
    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Override
    public String getWikipediaText(String title) {
        return cacheService.getCacheValueOrNew(getClass(), "CACHE_KEY_GET_WIKIPEDIA_TITLE", title,
            this::searchOnWikipediaByTitle);
    }

    private String searchOnWikipediaByTitle(String title){
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

    @Override
    public String getNearestWikipediaTitle(List<String> searchKeys) {
        final String titles = StrUtils.joinComma(searchKeys);
        return cacheService.getCacheValueOrNew(getClass(), "CACHE_KEY_NEAREST_WIKIPEDIA_TITLE", titles,
            this::searchNearestOnWikipedia);
    }

    private String searchNearestOnWikipedia(String titles){
        final String titleNotFound = String.format("Não encontardo artigos para os títulos/palavras chaves: %s", titles);
        try {
            List<SearchLike> searchResults = WebClient.builder().baseUrl(properties.getUrl())
                    .build().get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("action", properties.getAction())
                            .queryParam("prop", properties.getProp())
                            .queryParam("format", properties.getFormat())
                            .queryParam("explaintext", properties.getExplaintext())
                            .queryParam("list", "search")
                            .queryParam("srsearch", titles)
                            .build())
                    .retrieve()
                    .bodyToMono(WikipediaQueryLikeResponse.class)
                    .map(WikipediaQueryLikeResponse::query)
                    .map(QueryLikeResponse::search)
                    .block();

            if(CollectionUtils.isEmpty(searchResults))
                return titleNotFound;

            //return openAiService.findCorrectPlaceTitle(titles, searchResults);
            return null;
        } catch (Exception e) {
            log.error(titleNotFound, e);
            return titleNotFound;
        }
    }

}
