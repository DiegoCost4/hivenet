package com.hivenet.hivenet.dto;

import com.hivenet.hivenet.model.User;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class ProjectResponseDTO {
    private Long id;
    private String name;
    private String description;
    private String ownerEmail; // 🔹 Apenas o e-mail do dono
    private LocalDateTime createdAt;

    public ProjectResponseDTO(Long id, String name, String description, User owner, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ownerEmail = owner.getEmail();
        this.createdAt = createdAt;
    }
}
