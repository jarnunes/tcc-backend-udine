package com.pucminas.api;


import com.pucminas.integrations.openai.OpenAiService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/generateShortDescriptionFromPlacesName")
public class GenerateShortDescriptionFromPlacesName extends FunctionBase {

    private OpenAiService openAiService;

    @Autowired
    public void setOpenAiService(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    @PostMapping
    public ResponseEntity<String> apply(@RequestBody List<String> locationsName) {
        if (CollectionUtils.isEmpty(locationsName))
            return null;

        return ResponseEntity.ok(super.processRequest(locationsName, openAiService::generateShortDescription));
    }

    @Override
    protected String serviceName() {
        return "GenerateShortDescriptionFromPlacesName";
    }
}
