package com.portfolioproject.bugtracker.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CommentDTO {

    private Long id;
    private String comment;
    private UserDTO user;
    private TicketDTO ticket;
    private Date date;

    //Library that converts Time posted to how long it's been since the comment was posted
    //Implemented in comment service and converted to String
    private String prettyTime;

}
