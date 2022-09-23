package com.portfolioproject.bugtracker.mappers;

import com.portfolioproject.bugtracker.dto.CommentDTO;
import com.portfolioproject.bugtracker.entities.Comment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentDTO toCommentDTO(Comment comment);

    List<CommentDTO> toCommentDTOList(List<Comment> commentList);
}
