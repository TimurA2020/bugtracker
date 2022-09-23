package com.portfolioproject.bugtracker.api;

import com.portfolioproject.bugtracker.dto.CommentDTO;
import com.portfolioproject.bugtracker.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("api/comments")
@CrossOrigin
public class CommentController {
    @Autowired
    private CommentService commentService;

    //Getting comments for each ticket
    @GetMapping("{ticket_id}")
    public List<CommentDTO> getCommentsForTicket(@PathVariable(name = "ticket_id") Long id){
        return commentService.getCommentsFromTicket(id);
    }
}
