package com.pucminas.integrations.wikipedia;

import com.pucminas.commons.utils.MessageUtils;
import com.pucminas.commons.utils.StrUtils;
import com.pucminas.integrations.CacheService;
import com.pucminas.integrations.ServiceBase;
import com.pucminas.integrations.openai.OpenAiService;
import com.pucminas.integrations.wikipedia.dto.QueryLikeResponse;
import com.pucminas.integrations.wikipedia.dto.SearchLike;
import com.pucminas.integrations.wikipedia.dto.WikipediaQueryLikeResponse;
import com.pucminas.integrations.wikipedia.dto.WikipediaResponse;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@CommonsLog
public class WikipediaServiceImpl extends ServiceBase implements WikipediaService {

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
    protected String serviceNameKey() {
        return "wikipedia.service.name";
    }

    @Override
    public String getWikipediaText(String title) {
        return cacheService.getCachedValueOrNew(getClass(), "CACHE_KEY_GET_WIKIPEDIA_TITLE", title,
            this::searchOnWikipediaByTitle);
    }

    private String searchOnWikipediaByTitle(String title){
        return processWithAttempts(3, title, ()-> WebClient.builder().baseUrl(properties.getUrl())
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
                .block());
    }

    @Override
    public String getNearestWikipediaTitle(List<String> searchKeys) {
        final String titles = StrUtils.joinComma(searchKeys);
        return cacheService.getCachedValueOrNew(getClass(), "CACHE_KEY_NEAREST_WIKIPEDIA_TITLE", titles,
            this::searchNearestOnWikipedia);
    }

    private String searchNearestOnWikipedia(String titles){
        final List<SearchLike> searchResults = processWithAttempts(3, titles, () ->
                WebClient.builder().baseUrl(properties.getUrl())
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
                        .block());

        final double threshold = 0.8;
        final JaroWinklerDistance jaroWinkler = new JaroWinklerDistance();
        return searchResults.stream().map(SearchLike::title)
                .map(title -> Map.entry(title, jaroWinkler.apply(titles, title)))
                .filter(entry -> entry.getValue() >= threshold)
                .max(Comparator.comparingDouble(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .orElse(null);
    }

}
