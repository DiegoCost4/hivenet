package com.hivenet.hivenet.controller;

import com.hivenet.hivenet.dto.AuthRequest;
import com.hivenet.hivenet.dto.AuthResponse;
import com.hivenet.hivenet.security.JwtUtil;
import com.hivenet.hivenet.service.AuthService;
import com.hivenet.hivenet.service.PasswordResetService;
import com.hivenet.hivenet.service.RefreshTokenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordResetService passwordResetService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public AuthResponse register(@RequestBody AuthRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        return authService.login(request);
    }

    // 🔹 Endpoint para solicitar recuperação de senha
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request) {
        String token = passwordResetService.generateResetToken(request.get("email"));
        return ResponseEntity.ok("Token de recuperação gerado: " + token);
    }

    // 🔹 Endpoint para redefinir a senha com base no token
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
        String result = passwordResetService.resetPassword(request.get("token"), request.get("newPassword"));
        return ResponseEntity.ok(result);
    }

    // 🔄 Endpoint para renovar JWT usando Refresh Token
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
    
        return refreshTokenService.verifyToken(refreshToken)
                .map(token -> {
                    String newJwt = jwtUtil.generateToken(token.getUser().getEmail());
                    AuthResponse response = new AuthResponse(newJwt, refreshToken);
                    return ResponseEntity.ok(response);
                }).orElseThrow(() -> new RuntimeException("Refresh token inválido ou expirado!"));
    }
    
}
