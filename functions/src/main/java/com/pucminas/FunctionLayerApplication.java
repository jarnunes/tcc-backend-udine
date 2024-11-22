package com.pucminas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.pucminas"})
public class FunctionLayerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FunctionLayerApplication.class, args);
    }

}
