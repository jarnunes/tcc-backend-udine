Sim, � completamente poss�vel consumir as APIs do Google Cloud Speech-to-Text e Text-to-Speech a partir de uma aplica��o Spring Boot. O Google Cloud oferece bibliotecas de cliente em Java que facilitam o uso das suas APIs, incluindo o Speech-to-Text e o Text-to-Speech.

Como voc� j� est� utilizando o Google Places e o Vertex AI, voc� provavelmente j� tem as configura��es de autentica��o e o projeto no Google Cloud configurado. Vou te mostrar como voc� pode consumir essas APIs usando o Spring Boot.

Passos para configurar o Spring Boot com as APIs do Google Cloud
1. Depend�ncias do Maven
   Primeiro, voc� precisa adicionar as depend�ncias do cliente do Google Cloud para Speech-to-Text e Text-to-Speech no arquivo pom.xml da sua aplica��o Spring Boot.

Adicione as seguintes depend�ncias ao seu pom.xml:


````xml
<dependencies>
    <!-- Google Cloud Speech-to-Text -->
    <dependency>
        <groupId>com.google.cloud</groupId>
        <artifactId>google-cloud-speech</artifactId>
        <version>3.0.5</version> <!-- Verifique a vers�o mais recente -->
    </dependency>

    <!-- Google Cloud Text-to-Speech -->
    <dependency>
        <groupId>com.google.cloud</groupId>
        <artifactId>google-cloud-texttospeech</artifactId>
        <version>2.0.4</version> <!-- Verifique a vers�o mais recente -->
    </dependency>

    <!-- Depend�ncia para lidar com autentica��o do Google Cloud (se necess�rio) -->
    <dependency>
        <groupId>com.google.auth</groupId>
        <artifactId>google-auth-library-oauth2-http</artifactId>
        <version>1.10.0</version>
    </dependency>
</dependencies>

````

2. Configura��o da Autentica��o
   Para que sua aplica��o Spring Boot se autentique corretamente com o Google Cloud, voc� precisa configurar as credenciais do Google Cloud. O m�todo mais comum � usar uma chave de servi�o (arquivo JSON), que pode ser criada no Console do Google Cloud.

Passos para gerar a chave de servi�o (JSON):

Acesse o Google Cloud Console.
V� para o seu projeto.
Navegue at� IAM & Admin > Service Accounts.
Crie uma nova conta de servi�o e gere uma chave no formato JSON.
Baixe o arquivo JSON e configure a vari�vel de ambiente GOOGLE_APPLICATION_CREDENTIALS apontando para o caminho do arquivo.
No terminal, defina a vari�vel de ambiente:

bash
Copiar c�digo
>> export GOOGLE_APPLICATION_CREDENTIALS="/path/to/your-service-account-file.json"

Ou, se voc� estiver no Windows, defina a vari�vel com o comando:

bash
Copiar c�digo
>> set GOOGLE_APPLICATION_CREDENTIALS=C:\path\to\your-service-account-file.json
3. Exemplo de implementa��o no Spring Boot
   Aqui est�o dois exemplos de como voc� pode consumir as APIs do Google Cloud no seu aplicativo Spring Boot.

Exemplo 1: Speech-to-Text
Vamos criar um servi�o Spring Boot que usa a API do Google Cloud Speech-to-Text para transcrever um �udio.

Servi�o de Speech-to-Text
```java 
import com.google.cloud.speech.v1p1beta1.*;
import com.google.protobuf.ByteString;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class SpeechToTextService {

    public String transcribeAudio(String audioFilePath) throws IOException {
        // Crie um cliente para a API
        try (SpeechClient speechClient = SpeechClient.create()) {
            // L� o arquivo de �udio
            ByteString audioBytes = ByteString.readFrom(Files.newInputStream(Paths.get(audioFilePath)));

            // Configura��es para reconhecimento de fala
            RecognitionConfig recognitionConfig = RecognitionConfig.newBuilder()
                    .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16) // Formato de �udio
                    .setSampleRateHertz(16000) // Taxa de amostragem
                    .setLanguageCode("en-US") // Idioma
                    .build();

            // Configura��o do �udio a ser processado
            RecognitionAudio recognitionAudio = RecognitionAudio.newBuilder()
                    .setContent(audioBytes)
                    .build();

            // Realiza a transcri��o
            RecognizeResponse response = speechClient.recognize(recognitionConfig, recognitionAudio);

            // Retorna o texto transcrito
            StringBuilder transcription = new StringBuilder();
            for (SpeechRecognitionResult result : response.getResultsList()) {
                transcription.append(result.getAlternativesList().get(0).getTranscript());
            }
            return transcription.toString();
        }
    }
}

```

controller
```java 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class SpeechController {

    @Autowired
    private SpeechToTextService speechToTextService;

    @GetMapping("/transcribe")
    public String transcribe(@RequestParam String audioFilePath) {
        try {
            return speechToTextService.transcribeAudio(audioFilePath);
        } catch (IOException e) {
            return "Erro ao transcrever �udio: " + e.getMessage();
        }
    }
}

```

Exemplo 2: Text-to-Speech
Agora, vamos criar um servi�o Spring Boot para converter texto em fala usando a API do Google Cloud Text-to-Speech.

1. Servi�o de Text-to-Speech

````java 
import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.stereotype.Service;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@Service
public class TextToSpeechService {

    public void synthesizeTextToAudio(String text, String outputFilePath) throws Exception {
        // Cria o cliente da API Text-to-Speech
        try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create()) {
            // Configura a s�ntese de fala
            SynthesisInput input = SynthesisInput.newBuilder()
                    .setText(text)
                    .build();

            // Configura a voz (idioma, g�nero)
            VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                    .setLanguageCode("en-US") // Idioma
                    .setSsmlGender(SsmlVoiceGender.FEMALE) // G�nero da voz
                    .build();

            // Configura o �udio
            AudioConfig audioConfig = AudioConfig.newBuilder()
                    .setAudioEncoding(AudioEncoding.LINEAR16) // Formato de �udio
                    .build();

            // Realiza a s�ntese de fala
            SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);

            // Pega o �udio gerado
            ByteString audioContents = response.getAudioContent();

            // Salva o �udio em um arquivo
            try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(audioContents.toByteArray())) {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(byteArrayInputStream);
                AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, new java.io.File(outputFilePath));
            }
        }
    }
}

````

controller:

```java 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TextToSpeechController {

    @Autowired
    private TextToSpeechService textToSpeechService;

    @GetMapping("/synthesize")
    public String synthesize(@RequestParam String text, @RequestParam String outputFilePath) {
        try {
            textToSpeechService.synthesizeTextToAudio(text, outputFilePath);
            return "�udio gerado com sucesso em: " + outputFilePath;
        } catch (Exception e) {
            return "Erro ao gerar �udio: " + e.getMessage();
        }
    }
}

```