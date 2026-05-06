package me.minkyoung.buddy_back.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GoogleLoginRequest {

    @NotBlank(message = "idToken은 필수")
    private String idToken;
}
