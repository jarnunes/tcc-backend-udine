package com.pucminas.integrations.openai.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest {
    String role;
    String content;

    public MessageRequest(String content) {
        this.role = "user";
        this.content = content;
    }
}
