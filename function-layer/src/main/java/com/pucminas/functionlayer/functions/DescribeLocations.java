package com.pucminas.functionlayer.functions;


import com.pucminas.integrations.google.vertex.VertexAIService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
public class DescribeLocations implements Function<List<String>, String> {

    private VertexAIService vertexService;

    @Autowired
    public void setVertexService(VertexAIService vertexService) {
        this.vertexService = vertexService;
    }

    @Override
    public String apply(List<String> locationsName) {
        if (CollectionUtils.isEmpty(locationsName))
            return null;

        return vertexService.generateLocationDescription(locationsName);
    }
}
