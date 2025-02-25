package com.hivenet.hivenet.service;

import com.hivenet.hivenet.dto.ProjectResponseDTO;
import com.hivenet.hivenet.model.Project;
import com.hivenet.hivenet.model.User;
import com.hivenet.hivenet.repository.ProjectRepository;
import com.hivenet.hivenet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    public ProjectResponseDTO createProject(Project project) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        project.setOwner(user);
        project.setCreatedAt(LocalDateTime.now());
        Project savedProject = projectRepository.save(project);

        return new ProjectResponseDTO(
                savedProject.getId(),
                savedProject.getName(),
                savedProject.getDescription(),
                savedProject.getOwner(),
                savedProject.getCreatedAt());
    }

    public List<ProjectResponseDTO> getUserProjects() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        return projectRepository.findByOwner(user)
                .stream()
                .map(p -> new ProjectResponseDTO(
                        p.getId(),
                        p.getName(),
                        p.getDescription(),
                        p.getOwner(),
                        p.getCreatedAt()))
                .collect(Collectors.toList());
    }

    public Optional<ProjectResponseDTO> getProjectById(Long id) {
        return projectRepository.findById(id)
                .map(p -> new ProjectResponseDTO(
                        p.getId(),
                        p.getName(),
                        p.getDescription(),
                        p.getOwner(),
                        p.getCreatedAt()));
    }

    public ProjectResponseDTO updateProject(Long id, Project updatedProject) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        return projectRepository.findById(id)
                .filter(project -> project.getOwner().equals(user)) // ✅ Agora usa a variável 'user'
                .map(existingProject -> {
                    existingProject.setName(updatedProject.getName());
                    existingProject.setDescription(updatedProject.getDescription());
                    projectRepository.save(existingProject);

                    return new ProjectResponseDTO(
                            existingProject.getId(),
                            existingProject.getName(),
                            existingProject.getDescription(),
                            existingProject.getOwner(),
                            existingProject.getCreatedAt());
                }).orElseThrow(() -> new RuntimeException("Projeto não encontrado ou sem permissão para editar"));
    }

    public void deleteProject(Long id) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Projeto não encontrado"));
    
        // 🔹 Permite deletar apenas se o usuário autenticado for o dono do projeto
        if (!project.getOwner().equals(user)) {
            throw new RuntimeException("Você não tem permissão para excluir este projeto.");
        }
    
        projectRepository.delete(project);
    }
    

}
