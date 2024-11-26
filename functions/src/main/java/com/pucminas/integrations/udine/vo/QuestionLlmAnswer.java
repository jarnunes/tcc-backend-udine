package com.pucminas.integrations.udine.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionLlmAnswer {

    private String answer;
    private String idLocation;


    public static QuestionLlmAnswer LlmSample(){
        QuestionLlmAnswer sample = new QuestionLlmAnswer();
        sample.setAnswer("AQUI VEM SUA RESPOSTA");
        sample.setIdLocation("AQUI VEM O ID DO LOCAL");
        return sample;
    }
}


