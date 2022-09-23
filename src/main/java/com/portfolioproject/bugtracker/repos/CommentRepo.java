package com.portfolioproject.bugtracker.repos;

import com.portfolioproject.bugtracker.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface CommentRepo extends JpaRepository<Comment, Long> {

    List<Comment> findAllCommentByTicketId(@Param("id") Long id);

    List<Comment> findAllCommentByUserId(@Param("id") Long id);


}
