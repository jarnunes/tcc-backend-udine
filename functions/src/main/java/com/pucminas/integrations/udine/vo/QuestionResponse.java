package com.pucminas.integrations.udine.vo;

import com.pucminas.integrations.google.places.dto.PlacePhoto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = -2178099090696424614L;

    private String response;
    private QuestionFormatType formatType;
    private List<PlacePhoto> placePhotos = new ArrayList<>();
    private Boolean success = true;

    public static Builder builder(boolean isAudioQuestion, UnaryOperator<String> processAudioQuestion) {
        return new Builder(isAudioQuestion, processAudioQuestion);
    }

    public static class Builder {
        private final QuestionResponse questionResponse;
        private final boolean isAudioQuestion;
        private final UnaryOperator<String> processAudioQuestion;

        private Builder(boolean isAudioQuestion, UnaryOperator<String> processAudioQuestion) {
            this.questionResponse = new QuestionResponse();
            this.isAudioQuestion = isAudioQuestion;
            this.processAudioQuestion = processAudioQuestion;

            questionResponse.setFormatType(isAudioQuestion ? QuestionFormatType.AUDIO : QuestionFormatType.TEXT);
        }

        public Builder response(String response) {
            if (isAudioQuestion) {
                questionResponse.setResponse(processAudioQuestion.apply(response));
            } else {
                questionResponse.setResponse(response);
            }
            return this;
        }

        public Builder success(Boolean success) {
            questionResponse.setSuccess(success);
            return this;
        }

        public Builder placePhotos(List<PlacePhoto> placePhotos) {
            questionResponse.setPlacePhotos(placePhotos);
            return this;
        }

        public QuestionResponse build() {
            return questionResponse;
        }
    }
}