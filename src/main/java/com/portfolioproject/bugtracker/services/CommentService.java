package com.portfolioproject.bugtracker.services;

import com.ocpsoft.pretty.time.PrettyTime;
import com.portfolioproject.bugtracker.dto.CommentDTO;
import com.portfolioproject.bugtracker.entities.Comment;
import com.portfolioproject.bugtracker.mappers.CommentMapper;
import com.portfolioproject.bugtracker.repos.CommentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Comparator;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private CommentRepo commentRepo;

    public List<CommentDTO> getCommentsByPost(Long project_id){
        return commentMapper.toCommentDTOList(commentRepo.findAllCommentByTicketId(project_id));
    }

    public List<CommentDTO> getCommentsFromTicket(Long id){
        List<CommentDTO> commentDTOList = getCommentsByPost(id);
        for (int i = 0; i < commentDTOList.size(); i++){
            PrettyTime p = new PrettyTime();
            commentDTOList.get(i).setPrettyTime(p.format(commentDTOList.get(i).getDate()));
        }
        commentDTOList.sort(Comparator.comparing(CommentDTO::getDate).reversed());
        return commentDTOList;
    }

    public Comment addComment(Comment comment){
        return commentRepo.save(comment);
    }

    public Comment getComment(Long id){
        return commentRepo.findById(id).orElseThrow();
    }

    public void deleteComment(Comment comment){
        commentRepo.delete(comment);
    }

    public void deleteCommentsByAuthor(Long id){
        commentRepo.deleteAll(commentRepo.findAllCommentByUserId(id));
    }

}
