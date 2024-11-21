package com.pucminas.api;

import com.pucminas.integrations.wikipedia.WikipediaService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/searchWikipediaByTitle")
public class SearchWikipediaByTitle extends FunctionBase {

    private final WikipediaService service;

    @Autowired
    public SearchWikipediaByTitle(WikipediaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<String> apply(@RequestBody String s) {
        logRequest(s);

        if (StringUtils.isEmpty(s)) {
            return ResponseEntity.notFound().build();
        }

        final String response = service.getWikipediaText(s);
        logResponse(response);

        return ResponseEntity.ok(response);
    }


    @Override
    protected String serviceName() {
        return "SearchWikipediaByTitle";
    }
}
