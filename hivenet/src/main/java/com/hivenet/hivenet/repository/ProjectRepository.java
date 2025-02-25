package com.hivenet.hivenet.repository;

import com.hivenet.hivenet.model.Project;
import com.hivenet.hivenet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByOwner(User owner); // 🔹 Retorna os projetos de um usuário específico
}
