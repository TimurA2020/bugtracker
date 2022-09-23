package com.portfolioproject.bugtracker.api;

import com.portfolioproject.bugtracker.dto.TicketDTO;
import com.portfolioproject.bugtracker.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/api/tickets")
@CrossOrigin
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @GetMapping
    public List<TicketDTO> getAllTickets(){
        return ticketService.getTickets();
    }

    @GetMapping("{id}")
    public TicketDTO getTicket(@PathVariable(name = "id") Long id){
        return ticketService.getTicket(id);
    }

    //Getting tickets that are linked to a particular project
    @GetMapping("ticketsbyproject/{id}")
    public List<TicketDTO> getTicketsByProjectId(@PathVariable (name = "id") Long id){
        return ticketService.getTicketsByProject(id);
    }

    //Data for charts total
    @GetMapping("chartdatafortickets")
    public HashMap<String, Long> getChartData(){
        return ticketService.getGraphData();
    }

    //Data for charts per project
    @GetMapping("chartdatafortickets/{id}")
    public HashMap<String, Long> getChartDataByProjects(@PathVariable(name = "id") Long id){
        return ticketService.getGraphDataByProject(id);
    }

}
