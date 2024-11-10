package com.pucminas.udinetour;

import com.pucminas.udinetour.integration.google.vertex.VertexAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class UdineTourApplication {

    public static void main(String[] args) {
        SpringApplication.run(UdineTourApplication.class, args);
    }

    @Autowired
    private VertexAIService vertexService;

    @Bean
    public CommandLineRunner run(ApplicationContext context) {
        return args -> {
            String prompt = "Olá! Quero uma descrição resumida da praça Raul Soares em Belo Horizonte MG?";
            String response = vertexService.processPrompt(prompt);
            System.out.println(response);

        };
    }


}
