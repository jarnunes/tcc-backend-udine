package com.pucminas.api;

import com.pucminas.integrations.google.tech_to_speech.TextToSpeechService;
import com.pucminas.integrations.google.tech_to_speech.dto.TextToSpeechResponse;
import com.pucminas.integrations.openai.OpenAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/text-to-speech")
public class TextToSpeechRestController extends RestControllerBase {

    private TextToSpeechService textToSpeechService;
    private OpenAiService openAiService;

    @Autowired
    public void setTextToSpeechService(TextToSpeechService service) {
        this.textToSpeechService = service;
    }

    @Autowired
    public void setOpenAiService(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    @PostMapping("/short-description-from-places")
    public ResponseEntity<TextToSpeechResponse> generateShortDescription(@RequestBody List<String> placesNames) {
        final String description = super.processRequest(placesNames, openAiService::generateShortDescription);
        final TextToSpeechResponse response = super.processRequest(description, textToSpeechService::synthesizeText);
        return ResponseEntity.ok(response);
    }

    @Override
    protected String serviceName() {
        return "TextToSpeechRestController";
    }
}
