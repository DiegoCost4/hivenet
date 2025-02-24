package com.hivenet.hivenet.controller;

import com.hivenet.hivenet.model.User;
import com.hivenet.hivenet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // 🔹 1️⃣ LISTAR TODOS OS USUÁRIOS (GET)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')") // Apenas ADMIN pode ver todos os usuários
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    // 🔹 2️⃣ OBTER UM USUÁRIO PELO ID (GET)
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or authentication.name == @userRepository.findById(#id).get().email")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 🔹 3️⃣ ATUALIZAR USUÁRIO (PUT)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or authentication.name == @userRepository.findById(#id).get().email")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setEmail(updatedUser.getEmail());
                    user.setPassword(updatedUser.getPassword()); // Deveria ser criptografada antes de salvar
                    userRepository.save(user);
                    return ResponseEntity.ok(user);
                }).orElse(ResponseEntity.notFound().build());
    }

    // 🔹 4️⃣ EXCLUIR USUÁRIO (DELETE)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Apenas ADMIN pode excluir usuários
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.deleteById(id);
            return ResponseEntity.ok("Usuário removido com sucesso!");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
