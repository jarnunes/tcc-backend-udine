package com.pucminas.udinetour.functions;

import com.pucminas.udinetour.integration.google.vertex.VertexAIService;
import com.pucminas.udinetour.integration.google.vertex.dto.GeminiResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class DescribeLocation implements Function<String, String> {

    private VertexAIService vertexService;

    @Autowired
    public void setVertexService(VertexAIService vertexService) {
        this.vertexService = vertexService;
    }

    @Override
    public String apply(String locationName) {
        if(StringUtils.isEmpty(locationName))
            return null;

        return vertexService.generateLocationDescription(locationName);
    }
}
