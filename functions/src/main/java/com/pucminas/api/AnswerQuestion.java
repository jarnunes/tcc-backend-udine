package com.pucminas.api;

import com.pucminas.integrations.udine.QuestionServiceImpl;
import com.pucminas.integrations.udine.vo.QuestionRequest;
import com.pucminas.integrations.udine.vo.QuestionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/answerQuestion")
public class AnswerQuestion extends FunctionBase {

    private QuestionServiceImpl questionService;

    @Autowired
    public void setQuestionService(QuestionServiceImpl questionService) {
        this.questionService = questionService;
    }

    @PostMapping
    public ResponseEntity<QuestionResponse> apply(@RequestBody QuestionRequest request) {
        QuestionResponse response = super.processRequest(request, questionService::answerQuestion);
        return ResponseEntity.ok(response);
    }

    @Override
    protected String serviceName() {
        return "AnswerQuestion";
    }

}
