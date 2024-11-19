package com.pucminas.function.functions;


import com.pucminas.integrations.google.vertex.VertexAIService;
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
