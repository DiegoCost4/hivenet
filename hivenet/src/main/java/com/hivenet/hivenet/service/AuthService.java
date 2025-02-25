package com.hivenet.hivenet.service;

import com.hivenet.hivenet.dto.AuthRequest;
import com.hivenet.hivenet.dto.AuthResponse;
import com.hivenet.hivenet.model.RefreshToken;
import com.hivenet.hivenet.model.User;
import com.hivenet.hivenet.repository.UserRepository;
import com.hivenet.hivenet.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    public AuthResponse register(AuthRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole()); // 🔹 Define o tipo de usuário
        userRepository.save(user);
    
        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token);
    }

    public AuthResponse login(AuthRequest request) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
            String token = jwtUtil.generateToken(userDetails.getUsername());
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(request.getEmail());

            return new AuthResponse(token, refreshToken.getToken());
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Credenciais inválidas!");
        }
    }
}
