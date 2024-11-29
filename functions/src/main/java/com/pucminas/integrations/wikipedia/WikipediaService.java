package com.pucminas.integrations.wikipedia;

import com.pucminas.integrations.wikipedia.dto.SearchByTitleAndCity;
import com.pucminas.integrations.wikipedia.dto.SearchLike;

import java.util.List;

// https://stackoverflow.com/questions/7185288/how-can-i-get-wikipedia-content-using-wikipedias-api
public interface WikipediaService {


    String getWikipediaText(String title);

    List<SearchLike> getNearestWikipediaTitles(String name);


}
