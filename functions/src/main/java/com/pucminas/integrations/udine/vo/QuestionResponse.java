package com.pucminas.integrations.udine.vo;

import com.pucminas.commons.utils.MessageUtils;
import com.pucminas.commons.utils.StrUtils;
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


    public static Builder builder(boolean isAudioQuestion, UnaryOperator<String> processAudioQuestion) {
        return new Builder(isAudioQuestion, processAudioQuestion);
    }

    public static class Builder {
        private final QuestionResponse questionResponse;
        private final UnaryOperator<String> processAudioQuestion;

        private Builder(boolean isAudioQuestion, UnaryOperator<String> processAudioQuestion) {
            this.questionResponse = new QuestionResponse();
            this.processAudioQuestion = processAudioQuestion;

            questionResponse.setFormatType(isAudioQuestion ? QuestionFormatType.AUDIO : QuestionFormatType.TEXT);
        }

        public Builder responseMsgKey(String responseMsgKey, Object... args) {
            return response(MessageUtils.get(responseMsgKey, args));
        }

        public Builder response(String response) {
            if (questionResponse.formatType.isAudio()) {
                final String answer = StrUtils.removeMarkdownFormatting(response);
                questionResponse.setResponse(processAudioQuestion.apply(answer));
            } else {
                questionResponse.setResponse(response);
            }

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