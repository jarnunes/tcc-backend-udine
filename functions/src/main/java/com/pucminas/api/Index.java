package com.pucminas.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class Index {

    @GetMapping
    public String apply() {
        return "Welcome to udine tour!";
    }
}
