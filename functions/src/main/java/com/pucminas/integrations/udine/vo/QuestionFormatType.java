package com.pucminas.integrations.udine.vo;

public enum QuestionFormatType {
    TEXT,
    AUDIO;

    public boolean isAudio(){
        return this.equals(QuestionFormatType.AUDIO);
    }

    public boolean isText(){
        return this.equals(QuestionFormatType.TEXT);
    }
}
