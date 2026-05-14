package me.minkyoung.buddy_back.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;

@Component
public class GoogleTokenVerifier {

    @Value("${google.oauth.web-client-id}")
    private String webClientId;

    public GoogleIdToken.Payload verify(String idTokenString) {
        try {
            idTokenString = idTokenString.trim();
            String clientId = webClientId.trim();

            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance()
            )
                    .setAudience(Collections.singletonList(clientId))
                    .setIssuers(Arrays.asList(
                            "accounts.google.com",
                            "https://accounts.google.com"
                    ))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);

            if (idToken == null) {
                throw new IllegalArgumentException("유효하지 않은 Google ID 토큰입니다.");
            }

            GoogleIdToken.Payload payload = idToken.getPayload();

            System.out.println("Google 토큰 검증 성공");
            System.out.println("aud = [" + payload.getAudience() + "]");
            System.out.println("issuer = [" + payload.getIssuer() + "]");
            System.out.println("email = [" + payload.getEmail() + "]");
            System.out.println("exp = [" + payload.getExpirationTimeSeconds() + "]");
            System.out.println("현재 시간 = [" + (System.currentTimeMillis() / 1000) + "]");

            return payload;

        } catch (Exception e) {
            throw new IllegalArgumentException("Google 토큰 검증 실패", e);
        }

    }
}
