package com.hivenet.hivenet.service;

import com.hivenet.hivenet.model.Match;
import com.hivenet.hivenet.model.Project;
import com.hivenet.hivenet.model.User;
import com.hivenet.hivenet.repository.MatchRepository;
import com.hivenet.hivenet.repository.ProjectRepository;
import com.hivenet.hivenet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MatchService {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    public String likeProject(Long projectId) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        
        System.out.println("Usuário autenticado: " + userEmail); // 🔍 Verifica qual usuário está autenticado
        
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Projeto não encontrado"));
    
        // Impede o usuário de curtir o próprio projeto
        if (project.getOwner().equals(user)) {
            throw new RuntimeException("Você não pode curtir seu próprio projeto.");
        }
    
        Optional<Match> existingMatch = matchRepository.findByUserAndProject(user, project);
    
        if (existingMatch.isPresent()) {
            return "Você já curtiu este projeto.";
        }
    
        Match newMatch = new Match();
        newMatch.setUser(user);
        newMatch.setProject(project);
        newMatch.setMatched(false);
    
        matchRepository.save(newMatch);
    
        Optional<Match> oppositeMatch = matchRepository.findByUserAndProject(project.getOwner(), project);
        if (oppositeMatch.isPresent()) {
            newMatch.setMatched(true);
            oppositeMatch.get().setMatched(true);
            matchRepository.save(newMatch);
            matchRepository.save(oppositeMatch.get());
            return "🎉 Deu Match! Agora vocês podem se conectar!";
        }
    
        return "Curtida registrada! Aguarde a resposta.";
    }
    

    public List<Match> getMatchesForUser() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        return matchRepository.findByUserAndMatchedIsTrue(user);
    }
}
