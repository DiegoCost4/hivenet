package com.hivenet.hivenet.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "matches")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 🔹 Quem curtiu

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project; // 🔹 Projeto curtido

    private boolean matched; // 🔹 Indica se já houve um match
}
