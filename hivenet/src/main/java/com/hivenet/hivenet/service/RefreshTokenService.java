package com.hivenet.hivenet.service;

import com.hivenet.hivenet.model.RefreshToken;
import com.hivenet.hivenet.model.User;
import com.hivenet.hivenet.repository.RefreshTokenRepository;
import com.hivenet.hivenet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    public RefreshToken createRefreshToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Remove qualquer Refresh Token antigo
        refreshTokenRepository.findByUser(user).ifPresent(refreshTokenRepository::delete);

        // Cria um novo token válido por 7 dias
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(LocalDateTime.now().plusDays(7));

        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> verifyToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .filter(t -> t.getExpiryDate().isAfter(LocalDateTime.now())); // Verifica se ainda está válido
    }

    public void deleteRefreshToken(User user) {
        refreshTokenRepository.deleteByUser(user);
    }
}
