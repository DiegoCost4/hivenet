package com.hivenet.hivenet.service;

import com.hivenet.hivenet.model.PasswordResetToken;
import com.hivenet.hivenet.model.User;
import com.hivenet.hivenet.repository.PasswordResetTokenRepository;
import com.hivenet.hivenet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String generateResetToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpirationTime(LocalDateTime.now().plusHours(1)); // Expira em 1 hora

        tokenRepository.save(resetToken);
        return token;
    }

    public String resetPassword(String token, String newPassword) {
        Optional<PasswordResetToken> resetTokenOpt = tokenRepository.findByToken(token);

        if (resetTokenOpt.isEmpty() || resetTokenOpt.get().getExpirationTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token inválido ou expirado");
        }

        User user = resetTokenOpt.get().getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        tokenRepository.delete(resetTokenOpt.get()); // Remove o token usado

        return "Senha redefinida com sucesso!";
    }
}
