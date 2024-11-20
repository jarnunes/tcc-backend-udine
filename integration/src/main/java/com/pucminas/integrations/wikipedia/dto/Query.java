package com.pucminas.integrations.wikipedia.dto;

import java.util.Map;

public record Query(Map<String, Page> pages) {
}
