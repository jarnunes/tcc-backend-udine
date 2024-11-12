package com.pucminas.integrations.google.vertex.dto;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public abstract class GeminiContainerContents implements Serializable {

    List<GeminiContent> contents = new ArrayList<>();

    public void addContent(GeminiContent content) {
        if (this.contents == null) {
            this.contents = new ArrayList<>();
        }
        contents.add(content);
    }

}
