package com.portfolioproject.bugtracker.mappers;

import com.portfolioproject.bugtracker.dto.TicketDTO;
import com.portfolioproject.bugtracker.entities.Ticket;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TicketMapper {

    TicketDTO toTicketDTO(Ticket ticket);
    List<TicketDTO> toTicketDtoList(List<Ticket> ticketList);

}
