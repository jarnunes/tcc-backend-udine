package com.pucminas.function.functions;

import com.pucminas.integrations.google.tech_to_speech.TextToSpeechService;
import com.pucminas.integrations.google.tech_to_speech.dto.TextToSpeechResponse;
import com.pucminas.integrations.google.vertex.VertexAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
public class GenerateAudioDescriptionFromPlacesName implements Function<List<String>, TextToSpeechResponse> {

    private VertexAIService vertexAIService;
    private TextToSpeechService textToSpeechService;

    @Autowired
    public void setVertexAIService(VertexAIService service) {
        this.vertexAIService = service;
    }

    @Autowired
    public void setTextToSpeechService(TextToSpeechService service) {
        this.textToSpeechService = service;
    }

    @Override
    public TextToSpeechResponse apply(List<String> placesNames) {
        //final String fixDes = "A Praça Raul Soares, coração de Belo Horizonte, encanta com sua arquitetura clássica e o imponente Teatro Francisco Nunes.  Um espaço vibrante, ideal para um passeio tranquilo e apreciação da beleza urbana.\n\nA Praça da Estação, revitalizada e moderna,  resgata a memória ferroviária da cidade, combinando história e lazer em um ambiente acolhedor, perfeito para um encontro ou relaxamento.\n\nO Parque Municipal, um verdadeiro oásis verde no meio da cidade, oferece amplos espaços para atividades ao ar livre, trilhas e contato com a natureza. Um refúgio urbano para recarregar as energias.\n";
        final String description = vertexAIService.generateLocationDescription(placesNames);
        return textToSpeechService.synthesizeText(description);
    }
}
