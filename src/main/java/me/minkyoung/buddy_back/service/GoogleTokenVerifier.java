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

            GoogleIdToken parsedToken = GoogleIdToken.parse(
                    GsonFactory.getDefaultInstance(),
                    idTokenString
            );

            GoogleIdToken.Payload parsedPayload = parsedToken.getPayload();

            String tokenAud = parsedPayload.getAudience().toString();

            System.out.println("백엔드 webClientId = [" + clientId + "]");
            System.out.println("백엔드 webClientId 길이 = " + clientId.length());
            System.out.println("토큰 aud = [" + tokenAud + "]");
            System.out.println("토큰 aud 길이 = " + tokenAud.length());
            System.out.println("aud 일치 여부 = " + tokenAud.equals(clientId));
            System.out.println("issuer = [" + parsedPayload.getIssuer() + "]");
            System.out.println("email = [" + parsedPayload.getEmail() + "]");
            System.out.println("exp = [" + parsedPayload.getExpirationTimeSeconds() + "]");
            System.out.println("iat = [" + parsedPayload.getIssuedAtTimeSeconds() + "]");
            System.out.println("현재 시간 = [" + (System.currentTimeMillis() / 1000) + "]");
            String tokenInfoUrl = "https://oauth2.googleapis.com/tokeninfo?id_token=" + idTokenString;

            String tokenInfoResult = new RestTemplate().getForObject(tokenInfoUrl, String.class);

            System.out.println("Google tokeninfo 결과 = " + tokenInfoResult);
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
            return idToken.getPayload();
        } catch (Exception e) {
            throw new IllegalArgumentException("Google 토큰 검증 실패",e);
        }

    }
}
