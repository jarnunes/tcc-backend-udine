package com.pucminas.udinetour.integration.google.vertex.dto;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class GeminiRequest extends GeminiContainerContents implements Serializable {

    public static class GeminiRequestBuilder {
        private final GeminiRequest request;

        private GeminiRequestBuilder(GeminiRequest requestIn) {
            this.request = requestIn;
        }

        public static GeminiRequestBuilder prompt(final String prompt) {
            final GeminiRequest request = new GeminiRequest();
            final List<GeminiPart> parts = new ArrayList<>();
            parts.add(new GeminiPart(prompt));

            request.addContent(new GeminiContent(parts));
            return new GeminiRequestBuilder(request);
        }

        public GeminiRequest build() {
            return request;
        }
    }
}
