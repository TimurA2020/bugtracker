package com.portfolioproject.bugtracker.mappers;

import com.portfolioproject.bugtracker.dto.UserDTO;
import com.portfolioproject.bugtracker.entities.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toUserDto(User user);
    List<UserDTO> toUserDtoList(List<User> userList);
}
