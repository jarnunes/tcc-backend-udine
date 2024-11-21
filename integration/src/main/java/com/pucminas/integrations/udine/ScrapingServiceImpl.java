package com.pucminas.integrations.udine;

import lombok.extern.apachecommons.CommonsLog;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

@Component
@CommonsLog
public class ScrapingServiceImpl implements ScrapingService {

    // https://www.zenrows.com/blog/web-scraping-java#getting-started
    @Override
    public List<String> scrapAllTextTags(String url) {
        final List<String> tagsWithLongText = new ArrayList<>();

        return process(url, Stream.of("Nenhum elemento retornado").toList(), document -> {
            final Elements elements = document.select("p, div, span, h1, h2, h3, h4, h5, h6, article");

            for (Element element : elements) {
                final String text = element.text();
                final int wordCount = text.split("\\s+").length;

                if (wordCount > 20) {
                    tagsWithLongText.add(text);
                }
            }

            return tagsWithLongText;
        });

    }

    private <R> R process(final String url, final R defaultResponse,
        final Function<Document, R> function) {
        try {
            final Document document = Jsoup.connect(url).get();
            return function.apply(document);
        } catch (Exception e) {
            log.error("Erro ao extrair texto do site: " + url, e);
        }

        return defaultResponse;
    }
}
