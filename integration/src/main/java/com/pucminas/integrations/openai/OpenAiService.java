package com.pucminas.integrations.openai;

import com.pucminas.integrations.LlmService;
import com.pucminas.integrations.wikipedia.dto.SearchLike;

import java.util.List;

public interface OpenAiService extends LlmService {

    String processPrompt(String prompt);

    String findCorrectPlaceTitle(String searchTitle, List<SearchLike> searchResults);

}
