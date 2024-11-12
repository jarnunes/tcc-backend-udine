package com.pucminas.functionlayer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = {"com.pucminas"})
public class FunctionLayerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FunctionLayerApplication.class, args);
    }

}
