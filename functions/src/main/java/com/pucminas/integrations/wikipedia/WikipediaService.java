package com.pucminas.integrations.wikipedia;

import java.util.List;

// https://stackoverflow.com/questions/7185288/how-can-i-get-wikipedia-content-using-wikipedias-api
public interface WikipediaService {


    String getWikipediaText(String title);

    String getNearestWikipediaTitle(List<String> searchKeys);
}
