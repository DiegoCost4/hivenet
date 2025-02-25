package com.hivenet.hivenet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hivenet.hivenet.model.User;
import com.hivenet.hivenet.model.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUser(User user);
    void deleteByUser(User user);
}
