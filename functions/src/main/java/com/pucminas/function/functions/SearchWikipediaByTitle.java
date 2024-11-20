package com.pucminas.function.functions;

import com.pucminas.integrations.wikipedia.WikipediaService;
import com.pucminas.utils.MessageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.UnaryOperator;

@Component
public class SearchWikipediaByTitle implements UnaryOperator<String> {

    private final WikipediaService service;

    @Autowired
    public SearchWikipediaByTitle(WikipediaService service) {
        this.service = service;
    }

    @Override
    public String apply(String s) {
        return StringUtils.isEmpty(s) ? MessageUtils.get("wikipedia.empty.title") : service.getWikipediaText(s);
    }
}
