package com.hivenet.hivenet.dto;

import com.hivenet.hivenet.model.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {
    private String email;
    private String password;
    private Role role; // 🔹 Permite definir o tipo de usuário no registro
}
