package com.portfolioproject.bugtracker.repos;

import com.portfolioproject.bugtracker.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface RoleRepo extends JpaRepository<Role, Long> {

    Role findByRole(String role);
}
