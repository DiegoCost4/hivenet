package com.hivenet.hivenet.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String refreshToken;

    // 🔹 Adicionando um construtor que aceita apenas o token
    public AuthResponse(String token) {
        this.token = token;
        this.refreshToken = null; // 🔹 Evita problemas quando o refresh token não for necessário
    }
}

