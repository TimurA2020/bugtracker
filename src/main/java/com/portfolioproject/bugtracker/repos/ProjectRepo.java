package com.portfolioproject.bugtracker.repos;

import com.portfolioproject.bugtracker.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface ProjectRepo extends JpaRepository<Project, Long> {

    List<Project> findProjectsByAuthorId(@Param("id") Long id);
}
