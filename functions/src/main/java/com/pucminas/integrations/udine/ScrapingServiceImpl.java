package com.pucminas.integrations.udine;

import com.pucminas.integrations.CacheService;
import com.pucminas.integrations.ServiceBase;
import lombok.extern.apachecommons.CommonsLog;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@CommonsLog
public class ScrapingServiceImpl extends ServiceBase implements ScrapingService {

    private final CacheService cacheService;

    public ScrapingServiceImpl(CacheService cacheService) {
        super();
        this.cacheService = cacheService;
    }

    @Override
    protected String serviceNameKey() {
        return "scraping.service.name";
    }

    // https://www.zenrows.com/blog/web-scraping-java#getting-started
    @Override
    public List<String> scrapAllTextTags(String url) {
        return cacheService.getCachedValueOrNew(getClass(), "KEY_SCRAPING_SERVICE_SCRAP_ALL_TEXT_TAGS", url,
            this::scrapAllFromUrl);
    }

    private List<String> scrapAllFromUrl(String url){
        final List<String> tagsWithLongText = new ArrayList<>();
        final int wordsCount = 50;

        return processWithAttempts(3, url, () -> {
            final Document document = Jsoup.connect(url).get();
            final Elements elements = document.select("p, div, span, h1, h2, h3, h4, h5, h6, article");

            for (Element element : elements) {
                final String text = element.text();
                final int wordCount = text.split("\\s+").length;

                if (wordCount > wordsCount) {
                    tagsWithLongText.add(text);
                }
            }

            return tagsWithLongText;
        });
    }

}
