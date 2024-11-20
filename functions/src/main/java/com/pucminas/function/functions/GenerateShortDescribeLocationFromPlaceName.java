package com.pucminas.function.functions;


import com.pucminas.integrations.google.vertex.VertexAIService;
import com.pucminas.integrations.openai.OpenAiService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.UnaryOperator;

@Component
public class GenerateShortDescribeLocationFromPlaceName implements UnaryOperator<String> {

    private VertexAIService vertexService;
    private OpenAiService openAiService;

    @Autowired
    public void setVertexService(VertexAIService vertexService) {
        this.vertexService = vertexService;
    }

    @Autowired
    public void setOpenAiService(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    @Override
    public String apply(String locationName) {
        if (StringUtils.isEmpty(locationName))
            return null;

        return openAiService.generateShortDescription(locationName);
    }

}
