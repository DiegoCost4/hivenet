package com.hivenet.hivenet.repository;

import com.hivenet.hivenet.model.Match;
import com.hivenet.hivenet.model.Project;
import com.hivenet.hivenet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MatchRepository extends JpaRepository<Match, Long> {
    Optional<Match> findByUserAndProject(User user, Project project);
    List<Match> findByProject(Project project);
    List<Match> findByUserAndMatchedIsTrue(User user);
}
