package com.pucminas.function.functions;


import com.pucminas.integrations.openai.OpenAiService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.UnaryOperator;

//@Component
public class GenerateShortDescribeLocationFromPlaceName extends FunctionBase
    implements UnaryOperator<String> {

    private OpenAiService openAiService;

//    @Autowired
    public void setOpenAiService(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    @Override
    public String apply(String locationName) {
        if (StringUtils.isEmpty(locationName))
            return null;
        return super.processRequest(locationName, openAiService::generateShortDescription);
    }

    @Override
    protected String serviceName() {
        return "GenerateShortDescribeLocationFromPlaceName";
    }
}
