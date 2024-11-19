package com.pucminas.function.functions;


import com.pucminas.integrations.google.vertex.VertexAIService;
import com.pucminas.integrations.openai.OpenAiService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
public class DescribeLocations implements Function<List<String>, String> {

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
    public String apply(List<String> locationsName) {
        if (CollectionUtils.isEmpty(locationsName))
            return null;

        return vertexService.generateLocationDescription(locationsName);
//        return openAiService.generateLocationDescription(locationsName);
    }
}
