package com.portfolioproject.bugtracker.services;

import com.portfolioproject.bugtracker.dto.UserDTO;
import com.portfolioproject.bugtracker.entities.Role;
import com.portfolioproject.bugtracker.entities.User;
import com.portfolioproject.bugtracker.mappers.UserMapper;
import com.portfolioproject.bugtracker.repos.RoleRepo;
import com.portfolioproject.bugtracker.repos.TicketRepo;
import com.portfolioproject.bugtracker.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentService commentService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private ProjectService projectService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findAllByEmail(username);
        if(user != null){
            return user;
        }else{
            throw new UsernameNotFoundException("USER NOT FOUND");
        }
    }


    public List<UserDTO> getAllUserDtos(){
        List<User> users = userRepo.findAll();
        try{
//            Optional<User> optionalUser = users.stream().filter(a -> a.getEmail().equals("demodeveloper@bugtracker.io")).findFirst();
//            User user = optionalUser.get();
//            users.remove(user);
            List<User> demoUsers = users.stream().filter(a -> a.getFirstName().equals("Demo")).collect(Collectors.toList());
            users.removeAll(demoUsers);
        }catch (Exception e){
            e.printStackTrace();
        }
        return userMapper.toUserDtoList(users);
    }

    public UserDTO getUserDTO(Long id){
        return userMapper.toUserDto(userRepo.findById(id).orElse(null));
    }

    public User getUser(Long id){
        return userRepo.findById(id).orElseThrow();
    }

    public User registerUser(User user){

        User checkUser = userRepo.findAllByEmail(user.getEmail());
        if(checkUser == null){
            Role userRole = roleRepo.findByRole("ROLE_USER");
            ArrayList<Role> roles = new ArrayList<>();
            roles.add(userRole);
            user.setRoles(roles);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepo.save(user);
        }
        return null;
    }

    public String updatePassword(User user, String oldPassword, String newPassword){
        User currentUser = userRepo.findById(user.getId()).orElse(null);
        if(currentUser != null){
            if(passwordEncoder.matches(oldPassword, currentUser.getPassword())){
                currentUser.setPassword(passwordEncoder.encode(newPassword));
                userRepo.save(currentUser);
                return "redirect:/profile?passwordsuccess=true";
            }
        }
        return "redirect:/profile?errorcode=1";
    }

    public User updateRole(Long user_id, Long roleId){
        Role role = roleRepo.findById(roleId).orElse(null);
        User currentUser = userRepo.findById(user_id).orElse(null);
        if(currentUser != null){
            List<Role> userRoles = currentUser.getRoles();
            userRoles.add(role);
            currentUser.setRoles(userRoles);
            return userRepo.save(currentUser);
        }
        return null;
    }

    public User removeRole(Long user_id, Long roleId){
        Role role = roleRepo.findById(roleId).orElse(null);
        User currentUser = userRepo.findById(user_id).orElse(null);
        if(currentUser != null){
            List<Role> userRoles = currentUser.getRoles();
            userRoles.remove(role);
            currentUser.setRoles(userRoles);
            return userRepo.save(currentUser);
        }
        return null;
    }

    public void deleteUser(Long user_id){
        User currentUser = userRepo.findById(user_id).orElseThrow();
        List<Role> roles = currentUser.getRoles();
        roles.clear();
        currentUser.setRoles(roles);
        commentService.deleteCommentsByAuthor(user_id);
        ticketService.deleteTicketsByUserId(user_id);
        projectService.deleteProjectsByUserId(user_id);
        userRepo.delete(currentUser);
    }

    public void updateCredentials(User user, String firstName, String lastName){
        user.setFirstName(firstName);
        user.setLastName(lastName);
        userRepo.save(user);
    }

    public User saveUser(User user){
        return userRepo.save(user);
    }

    public List<UserDTO> getAllDevelopers(){
        List<User> users = userRepo.findUserByRole(2L);
        try {
            Optional<User> optionalUser = users.stream().filter(a -> a.getEmail().equals("demodeveloper@bugtracker.io")).findFirst();
            User user = optionalUser.get();
            users.remove(user);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("couldn't find the demoaccount user");
        }
        return userMapper.toUserDtoList(users);
    }

    public List<UserDTO> getAllPMs(){
        List<User> users = userRepo.findUserByRole(4L);
        try {
            Optional<User> optionalUser = users.stream().filter(a -> a.getEmail().equals("demopm@bugtracker.io")).findFirst();
            Optional<User> optionalAdmin = users.stream().filter(a -> a.getEmail().equals("demoadmin@bugtracker.io")).findFirst();
            User user = optionalUser.get();
            User admin = optionalAdmin.get();
            users.remove(user);
            users.remove(admin);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("couldn't find the demoaccount user");
        }
        return userMapper.toUserDtoList(users);
    }

    public long countAllUsers(){

        return userRepo.count() - 4;
    }
}
