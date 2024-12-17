package com.pucminas.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.victools.jsonschema.generator.*;
import com.pucminas.commons.utils.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/documentation")
public class Documentation {

    RequestMappingHandlerMapping handlerMapping;

    @Autowired
    public void setHandlerMapping(RequestMappingHandlerMapping requestMappingHandlerMapping) {
        this.handlerMapping = requestMappingHandlerMapping;
    }

    @GetMapping("/endpoints")
    public List<Map<String, Object>> registeredEndpoints() {
        final SchemaGenerator generator = createSchemaGenerator();
        final List<Map<String, Object>> endpointsList = new ArrayList<>();

        getHandlerMethods().forEach(entry -> {
            Map<String, Object> mapa = new HashMap<>();
            mapa.put("endpoint", entry.getKey().getPathPatternsCondition().getPatterns().iterator().next().getPatternString());
            mapa.put("method", entry.getKey().getMethodsCondition().getMethods().iterator().next().name());

            final Parameter parameter = ListUtils.firstOrNull(entry.getValue().getMethod().getParameters());
            if (parameter != null) {
                final JsonNode jsonSchema = generator.generateSchema(parameter.getType());
                mapa.put("request-body-documentation", jsonSchema);
            }
            endpointsList.add(mapa);
        });

        return endpointsList;
    }

    private SchemaGenerator createSchemaGenerator() {
        final SchemaGeneratorConfigBuilder configBuilder = new SchemaGeneratorConfigBuilder(
                SchemaVersion.DRAFT_2020_12, OptionPreset.PLAIN_JSON);
        final SchemaGeneratorConfig config = configBuilder
                .with(Option.EXTRA_OPEN_API_FORMAT_VALUES)
                .without(Option.FLATTENED_ENUMS_FROM_TOSTRING).build();
        return new SchemaGenerator(config);
    }

    private Stream<Map.Entry<RequestMappingInfo, HandlerMethod>> getHandlerMethods() {
        return handlerMapping.getHandlerMethods().entrySet().stream()
                .filter(this::pathPathConditionNonNull)
                .filter(this::methodConditionNonEmpty);
    }

    private boolean pathPathConditionNonNull(Map.Entry<RequestMappingInfo, HandlerMethod> request) {
        return request.getKey().getPathPatternsCondition() != null;
    }

    private boolean methodConditionNonEmpty(Map.Entry<RequestMappingInfo, HandlerMethod> request) {
        return !request.getKey().getMethodsCondition().isEmpty();
    }
}
