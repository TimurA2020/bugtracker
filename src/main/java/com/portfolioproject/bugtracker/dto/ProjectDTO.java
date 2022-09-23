package com.portfolioproject.bugtracker.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectDTO {

    private Long id;
    private String name;
    private String content;
    private UserDTO author;

}
