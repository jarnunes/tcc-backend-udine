package com.pucminas.functionlayer;

import com.pucminas.integrations.google.speech_to_text.SpeechToTextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

@SpringBootApplication(scanBasePackages = {"com.pucminas"})
public class FunctionLayerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FunctionLayerApplication.class, args);
    }

    @Autowired
    private SpeechToTextService service;

    @Bean
    public CommandLineRunner execute() {
        return args -> {
            System.out.println("Function Layer is running!");

            File audioFile = new File("C:\\Users\\jarnu\\Downloads\\audio_whatsapp.ogg");
            System.out.println(service.speechToText(convertOggToBase64(audioFile)));
        };
    }


    public String convertOggToBase64(File audioFile) {
        try (FileInputStream fileInputStream = new FileInputStream(audioFile);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }

            // Codificar o conteúdo do arquivo OGG em Base64
            byte[] audioBytes = byteArrayOutputStream.toByteArray();
            return Base64.getEncoder().encodeToString(audioBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
