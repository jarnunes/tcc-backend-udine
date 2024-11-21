package com.pucminas.function.functions;

import com.pucminas.integrations.udine.QuestionServiceImpl;
import com.pucminas.integrations.udine.vo.QuestionRequest;
import com.pucminas.integrations.udine.vo.QuestionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

//@Component
public class AnswerQuestion extends FunctionBase implements Function<QuestionRequest, QuestionResponse> {

    private QuestionServiceImpl questionService;

//    @Autowired
    public void setQuestionService(QuestionServiceImpl questionService) {
        this.questionService = questionService;
    }

    @Override
    public QuestionResponse apply(QuestionRequest request) {
        return super.processRequest(request, questionService::answerQuestion);
    }

    @Override
    protected String serviceName() {
        return "AnswerQuestion";
    }

}
