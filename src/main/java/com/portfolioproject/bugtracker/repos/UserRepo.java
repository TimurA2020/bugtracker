package com.portfolioproject.bugtracker.repos;

import com.portfolioproject.bugtracker.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface UserRepo extends JpaRepository<User, Long> {

    User findAllByEmail(String email);

    @Query("SELECT user FROM User user LEFT JOIN user.roles role WHERE role.id = ?1")
    List<User> findUserByRole(Long role);
}
