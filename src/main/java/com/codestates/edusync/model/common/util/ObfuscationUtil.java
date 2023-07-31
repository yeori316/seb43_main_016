package com.codestates.edusync.model.common.util;

import org.springframework.stereotype.Component;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class ObfuscationUtil {

    /**
     * Base64 Decoder
     * @param message Encoded Message
     * @return Decoded Message
     */
    public String getDecoded(String message) {
        String decode = URLDecoder.decode(message, StandardCharsets.UTF_8);
        Base64.Decoder decoder = Base64.getDecoder();
        return new String(decoder.decode(decode.getBytes()));
    }
}
