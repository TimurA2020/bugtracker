package com.portfolioproject.bugtracker.services;

import com.portfolioproject.bugtracker.dto.TicketDTO;
import com.portfolioproject.bugtracker.entities.Comment;
import com.portfolioproject.bugtracker.entities.Ticket;
import com.portfolioproject.bugtracker.mappers.TicketMapper;
import com.portfolioproject.bugtracker.repos.CommentRepo;
import com.portfolioproject.bugtracker.repos.TicketRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class TicketService {

    @Autowired
    private TicketMapper ticketMapper;

    @Autowired
    private TicketRepo ticketRepo;

    @Autowired
    private CommentRepo commentRepo;

    public TicketDTO getTicket(Long id){
        return ticketMapper.toTicketDTO(ticketRepo.findById(id).orElseThrow());
    }

    public List<TicketDTO> getTickets(){
        return ticketMapper.toTicketDtoList(ticketRepo.findAll());
    }

    public List<TicketDTO> getTicketsByProject(Long id){
        return ticketMapper.toTicketDtoList(ticketRepo.findAllTicketByProjectId(id));
    }

    public void deleteTicketEntitiesByProject(Long id){
        List<Ticket> tickets = ticketRepo.findAllTicketByProjectId(id);
        for (int i = 0; i < tickets.size(); i++){
            deleteTicket(tickets.get(i).getId());
        }
    }

    public Ticket getTicketEntity(Long id){
        return ticketRepo.findById(id).orElseThrow();
    }

    public Ticket addTicket(Ticket ticket){
        ticket.setStatus("New");
        return ticketRepo.save(ticket);
    }

    public void deleteTicket(Long ticket_id){
        Ticket ticket = ticketRepo.findById(ticket_id).orElseThrow();
        List<Comment> comments = commentRepo.findAllCommentByTicketId(ticket_id);
        commentRepo.deleteAll(comments);
        ticketRepo.delete(ticket);
    }

    public void deleteTicketsByUserId(Long user_id){
        List<Ticket> tickets = ticketRepo.findTicketsByTicketAuthorId(user_id);
        ticketRepo.deleteAll(tickets);
    }

    public void changeStatus(Ticket ticket, String status){
        ticket.setStatus(status);
        ticketRepo.save(ticket);
    }
    public HashMap<String, Long> getGraphData() {
        List<TicketDTO> tickets = getTickets();
        HashMap<String, Long> map = new HashMap<>();
        map.put("High", tickets.stream().filter(t -> t.getPriority().equals("High")).count());
        map.put("Medium", tickets.stream().filter(t -> t.getPriority().equals("Medium")).count());
        map.put("Low", tickets.stream().filter(t -> t.getPriority().equals("Low")).count());
        map.put("Bugs", tickets.stream().filter(t -> t.getType().equals("Bug")).count());
        map.put("Features", tickets.stream().filter(t -> t.getType().equals("Feature")).count());
        map.put("Issues", tickets.stream().filter(t -> t.getType().equals("Issue")).count());
        map.put("Other", tickets.stream().filter(t -> t.getType().equals("Other")).count());
        map.put("New", tickets.stream().filter(t -> t.getStatus().equals("New")).count());
        map.put("In-Progress", tickets.stream().filter(t -> t.getStatus().equals("In-Progress")).count());
        map.put("Resolved", tickets.stream().filter(t -> t.getStatus().equals("Resolved")).count());

        return map;
    }

    public HashMap<String, Long> getGraphDataByProject(Long project_id) {
        List<TicketDTO> tickets = getTicketsByProject(project_id);
        HashMap<String, Long> map = new HashMap<>();
        map.put("High", tickets.stream().filter(t -> t.getPriority().equals("High")).count());
        map.put("Medium", tickets.stream().filter(t -> t.getPriority().equals("Medium")).count());
        map.put("Low", tickets.stream().filter(t -> t.getPriority().equals("Low")).count());
        map.put("Bugs", tickets.stream().filter(t -> t.getType().equals("Bug")).count());
        map.put("Features", tickets.stream().filter(t -> t.getType().equals("Feature")).count());
        map.put("Issues", tickets.stream().filter(t -> t.getType().equals("Issue")).count());
        map.put("Other", tickets.stream().filter(t -> t.getType().equals("Other")).count());
        map.put("New", tickets.stream().filter(t -> t.getStatus().equals("New")).count());
        map.put("In-Progress", tickets.stream().filter(t -> t.getStatus().equals("In-Progress")).count());
        map.put("Resolved", tickets.stream().filter(t -> t.getStatus().equals("Resolved")).count());

        return map;
    }


    public long countAllTickets(){
        return ticketRepo.count();
    }

}
