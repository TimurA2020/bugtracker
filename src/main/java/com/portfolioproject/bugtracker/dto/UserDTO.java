package com.portfolioproject.bugtracker.dto;

import com.portfolioproject.bugtracker.entities.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDTO {
    private long id;
    private String email;
    private String firstName;
    private String lastName;
    private List<Role> roles;
    private String profilePic;
}
