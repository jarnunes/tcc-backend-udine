package com.pucminas.integrations.google.speech_to_text.dto;

import java.io.Serializable;

public enum AudioEncodingType implements Serializable {

    ENCODING_UNSPECIFIED,
    LINEAR16,
    FLAC,
    MULAW,
    AMR,
    AMR_WB,
    OGG_OPUS,
    SPEEX_WITH_HEADER_BYTE,
    MP3,
    WEBM_OPUS,
    UNRECOGNIZED;
}
