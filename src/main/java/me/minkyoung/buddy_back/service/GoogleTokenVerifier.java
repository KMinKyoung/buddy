package me.minkyoung.buddy_back.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import com.google.api.client.util.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class GoogleTokenVerifier {

    @Value("${google.oauth.web-client-id}")
    private String webClientId;

    public GoogleIdToken.Payload verify(String idTokenString) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    GsonFactory.getDefaultInstance()
            )
                    .setAudience(Collections.singletonList(webClientId))
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
