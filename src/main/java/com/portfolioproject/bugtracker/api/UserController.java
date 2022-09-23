package com.portfolioproject.bugtracker.api;

import com.portfolioproject.bugtracker.dto.UserDTO;
import com.portfolioproject.bugtracker.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/users")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserDTO> getUsers(){
        return userService.getAllUserDtos();
    }

    @GetMapping("{id}")
    public UserDTO getUser(@PathVariable (name = "id") Long id){
        return userService.getUserDTO(id);
    }

    @GetMapping("/projectmanagers")
    public List<UserDTO> getPMs(){
        return userService.getAllPMs();
    }

}
