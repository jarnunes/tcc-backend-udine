package com.pucminas.integrations.wikipedia.dto;

import java.util.Map;

public record QueryPagesResponse(Map<String, Page> pages) {
}
