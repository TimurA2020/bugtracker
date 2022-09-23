package com.portfolioproject.bugtracker.repos;

import com.portfolioproject.bugtracker.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface TicketRepo extends JpaRepository<Ticket, Long> {

    List<Ticket> findAllTicketByProjectId(@Param("id") Long id);

    List<Ticket> findTicketsByTicketAuthorId(@Param("id") Long id);
}
