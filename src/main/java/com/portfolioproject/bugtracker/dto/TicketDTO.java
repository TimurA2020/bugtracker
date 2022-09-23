package com.portfolioproject.bugtracker.dto;

import com.portfolioproject.bugtracker.entities.Ticket;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketDTO {

    private Long id;
    private String title;
    private String description;
    private String type;
    private String priority;
    private String status;
    private UserDTO ticketAuthor;
    private UserDTO assignedDeveloper;
    private ProjectDTO project;


}
