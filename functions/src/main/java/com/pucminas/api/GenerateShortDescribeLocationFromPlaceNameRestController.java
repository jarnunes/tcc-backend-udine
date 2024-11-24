package com.pucminas.api;


import com.pucminas.integrations.openai.OpenAiService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/generateShortDescribeLocationFromPlaceName")
public class GenerateShortDescribeLocationFromPlaceNameRestController extends RestControllerBase {

    private OpenAiService openAiService;

    @Autowired
    public void setOpenAiService(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    @PostMapping
    public ResponseEntity<String> apply(@RequestBody String locationName) {
        if (StringUtils.isEmpty(locationName))
            return null;
        return ResponseEntity.ok(super.processRequest(locationName, openAiService::generateShortDescription));
    }

    @Override
    protected String serviceName() {
        return "GenerateShortDescribeLocationFromPlaceNameRestController";
    }
}
