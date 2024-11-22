package com.pucminas.integrations.wikipedia;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties(prefix = "udine.wikipedia")
public class WikipediaProperties {

    @Value("${udine.wikipedia.url}")
    private String url;

    @Value("${udine.wikipedia.action}")
    private String action;

    @Value("${udine.wikipedia.prop}")
    private String prop;

    @Value("${udine.wikipedia.format}")
    private String format;

    @Value("${udine.wikipedia.explaintext}")
    private String explaintext;
}
