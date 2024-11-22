package com.pucminas.integrations.wikipedia;

import com.pucminas.integrations.wikipedia.dto.SearchByTitleAndCity;

// https://stackoverflow.com/questions/7185288/how-can-i-get-wikipedia-content-using-wikipedias-api
public interface WikipediaService {


    String getWikipediaText(String title);

    String getNearestWikipediaTitle(SearchByTitleAndCity filter);
}
