package com.pucminas.integrations.udine;

import com.pucminas.integrations.udine.vo.QuestionRequest;
import com.pucminas.integrations.udine.vo.QuestionResponse;

public interface QuestionService {

    QuestionResponse answerQuestion(QuestionRequest questionRequest);
}
