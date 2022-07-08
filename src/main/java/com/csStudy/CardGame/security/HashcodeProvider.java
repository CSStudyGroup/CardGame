package com.csStudy.CardGame.security;

import org.springframework.http.server.ServerHttpAsyncRequestControl;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@Component
public class HashcodeProvider {
    public String generateHashcode(String prefix, String key) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");

            messageDigest.update(key.getBytes(StandardCharsets.UTF_8));

            return prefix + "_" + new String(Hex.encode(messageDigest.digest()));
        } catch(NoSuchAlgorithmException e) {
            System.out.println("예외처리: No such Algorithm");
            return "";
        }
    }
}
