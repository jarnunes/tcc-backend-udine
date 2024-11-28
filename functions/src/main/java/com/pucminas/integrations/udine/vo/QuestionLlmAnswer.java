package com.pucminas.integrations.udine.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionLlmAnswer {

    private String answer;
    private List<String> idLocations;

    public static QuestionLlmAnswer llmSample(){
        QuestionLlmAnswer sample = new QuestionLlmAnswer();
        sample.setAnswer("AQUI VEM SUA RESPOSTA");
        sample.setIdLocations(List.of("AQUI VEM O(S) ID(S) DO LOCAL"));
        return sample;
    }
}


