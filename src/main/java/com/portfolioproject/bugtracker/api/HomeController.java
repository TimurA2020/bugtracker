package com.portfolioproject.bugtracker.api;

import com.portfolioproject.bugtracker.dto.TicketDTO;
import com.portfolioproject.bugtracker.dto.UserDTO;
import com.portfolioproject.bugtracker.entities.Comment;
import com.portfolioproject.bugtracker.entities.Project;
import com.portfolioproject.bugtracker.entities.Ticket;
import com.portfolioproject.bugtracker.entities.User;
import com.portfolioproject.bugtracker.services.*;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;


@Controller
public class HomeController {

    //Services
    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private FileUploadService fileUploadService;

    @Value("${uploadURL}")
    private String fileUploadURL;

    @Value("${targetURL}")
    private String targetURL;

    //Registration and login

    @PreAuthorize("isAnonymous()")
    @GetMapping("/register")
    public String register(){
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam(name = "email") String email,
                           @RequestParam(name = "password") String password,
                           @RequestParam(name = "firstName") String firstName,
                           @RequestParam(name = "lastName") String lastName,
                           @RequestParam(name = "password2") String password2){
        if(password.equals(password2)){
            User user = new User();
            user.setEmail(email);
            user.setPassword(password);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user = userService.registerUser(user);
            if(user != null){
                return "redirect:/login?success=true";
            }
            if(user == null){
                return "redirect:/register?usererror=true";
            }
        }
        return "redirect:/register?passworderror=true";

    }

    @PreAuthorize("isAnonymous()")
    @GetMapping(value = "/login")
    public String getLogin(){
        return "login";
    }

    //MVC for users

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/")
    public String getIndex(){
        return "indexnew";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/ticketdetails/{id}")
    public String ticketDetails(@PathVariable(name = "id") Long id, Model model){
        TicketDTO ticketDTO = ticketService.getTicket(id);
        model.addAttribute("ticket_id", id);
        model.addAttribute("ticket", ticketDTO);
        model.addAttribute("user", getCurrentUser());
        return "ticketdetails";
    }


    @GetMapping("/indexnew")
    public String indexnew(){
        return "indexnew";
    }



    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/profile")
    public String getProfile(Model model){
        model.addAttribute("user_id", getCurrentUser().getId());
        model.addAttribute("user", getCurrentUser());
        return "profile";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/projects/{id}")
    public String getProjectDetails(@PathVariable(name = "id") Long id, Model model){
        List<UserDTO> developers = userService.getAllDevelopers();
        model.addAttribute("developers", developers);
        model.addAttribute("id", id);
        return "projectdetails";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/alltickets")
    public String getAllTickets(){
        return "alltickets";
    }


    @PreAuthorize("isAnonymous()")
    @GetMapping("/demoaccount")
    public String getDemoAccount(){
        return "demoaccount";
    }

    //admin and pms mvc

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping(value = "/admin")
    public String getAdmin(Model model){
        return "admin";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping(value = "/userdetails/{id}")
    public String userDetails(){
        return "userdetails";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PROJECT_MANAGER')")
    @GetMapping("/adminpanel")
    public String adminPanel(Model model){
        long projectCount = projectService.countAllProjects();
        long userCount = userService.countAllUsers();
        long ticketCount = ticketService.countAllTickets();
        model.addAttribute("projectcount", projectCount);
        model.addAttribute("usercount", userCount);
        model.addAttribute("ticketcount", ticketCount);
        return "adminpanel";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PROJECT_MANAGER')")
    @GetMapping("/projectsadmin")
    public String projectsAdmin(){
        return "projectsadmin";
    }


    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PROJECT_MANAGER')")
    @GetMapping("/editproject/{id}")
    public String editProject(@PathVariable(name = "id") Long id,
                              Model model){
        Long author = projectService.getProject(id).getAuthor().getId();
        List<UserDTO> pms = userService.getAllPMs();
        model.addAttribute("pms", pms);
        model.addAttribute("author", author);
        return "editproject";
    }

    //add comments, tickets, projects etc.

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/addrole")
    public String addRole(@RequestParam(name = "user_id") Long user_id,
                          @RequestParam(name = "role_id") Long role_id){
        User user = getCurrentUser();
        if (user.getEmail().equals("demoadmin@bugtracker.io")){
            return "redirect:/demoaccounterror";
        }

        userService.updateRole(user_id, role_id);
        return "redirect:/userdetails/" + user_id;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/addcomment")
    public String addComment(@RequestParam(name = "ticket_id") Long ticket_id,
                             @RequestParam(name = "comment") String comment_text){

        User user = getCurrentUser();
        if (user.getEmail().equals("demoadmin@bugtracker.io") || user.getEmail().equals("demouser@bugtracker.io") ||
                user.getEmail().equals("demodeveloper@bugtracker.io") || user.getEmail().equals("demopm@bugtracker.io")){
            return "redirect:/demoaccounterror";
        }

        Comment comment = new Comment();
        comment.setComment(comment_text);
        comment.setTicket(ticketService.getTicketEntity(ticket_id));
        Date date = new Date();
        comment.setDate(date);
        comment.setUser(getCurrentUser());
        commentService.addComment(comment);
        return "redirect:/ticketdetails/" + ticket_id;
    }


    @PreAuthorize("hasAnyRole('ROLE_PROJECT_MANAGER', 'ROLE_ADMIN')")
    @PostMapping("/addproject")
    public String addProject(@RequestParam(name = "title") String title,
                             @RequestParam(name = "description") String description){
        User user = getCurrentUser();
        if (user.getEmail().equals("demoadmin@bugtracker.io") || user.getEmail().equals("demouser@bugtracker.io") ||
                user.getEmail().equals("demodeveloper@bugtracker.io") || user.getEmail().equals("demopm@bugtracker.io")){
            return "redirect:/demoaccounterror";
        }

        Project project = new Project();
        project.setAuthor(getCurrentUser());
        project.setContent(description);
        project.setName(title);
        projectService.addProject(project);
        return "redirect:/";
    }

    @PreAuthorize("hasAnyRole('ROLE_DEVELOPER', 'ROLE_ADMIN', 'ROLE_PROJECT_MANAGER')")
    @PostMapping("/addticket")
    public String addTicket(@RequestParam(name = "project_id") Long project_id,
                            @RequestParam(name = "title") String title,
                            @RequestParam(name = "description") String description,
                            @RequestParam(name = "type") String type,
                            @RequestParam(name = "priority") String priority,
                            @RequestParam(name = "dev") Long dev){
        User user = getCurrentUser();
        if (user.getEmail().equals("demoadmin@bugtracker.io") || user.getEmail().equals("demouser@bugtracker.io") ||
                user.getEmail().equals("demodeveloper@bugtracker.io") || user.getEmail().equals("demopm@bugtracker.io")) {
            return "redirect:/demoaccounterror";
        }
        Ticket ticket = new Ticket();
        ticket.setTicketAuthor(getCurrentUser());
        ticket.setTitle(title);
        ticket.setDescription(description);
        ticket.setPriority(priority);
        ticket.setProject(projectService.getProjectEntity(project_id));
        ticket.setAssignedDeveloper(userService.getUser(dev));
        ticket.setType(type);
        ticketService.addTicket(ticket);

        return "redirect:/projects/" + project_id;
    }

    //deleting

   // @PreAuthorize("@authComponent.hasPermission(#ticket_id)")
    @PreAuthorize("hasAnyRole('ROLE_DEVELOPER', 'ROLE_ADMIN', 'ROLE_PROJECT_MANAGER')")
    @PostMapping("/deleteticket")
    public String deleteTicket(@RequestParam(name = "ticket_id") Long ticket_id){
        User user = getCurrentUser();
        if (user.getEmail().equals("demoadmin@bugtracker.io") || user.getEmail().equals("demouser@bugtracker.io") ||
                user.getEmail().equals("demodeveloper@bugtracker.io") || user.getEmail().equals("demopm@bugtracker.io")){
            return "redirect:/demoaccounterror";
        }
        Long project_id = ticketService.getTicket(ticket_id).getProject().getId();
        ticketService.deleteTicket(ticket_id);
        return "redirect:/projects/" + project_id;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/deletecomment")
    public String deleteComment(@RequestParam(name = "comment_id") Long comment_id){
        User user = getCurrentUser();
        if (user.getEmail().equals("demoadmin@bugtracker.io") || user.getEmail().equals("demouser@bugtracker.io") ||
                user.getEmail().equals("demodeveloper@bugtracker.io") || user.getEmail().equals("demopm@bugtracker.io")){
            return "redirect:/demoaccounterror";
        }
        Comment comment = commentService.getComment(comment_id);
        Long ticket_id = comment.getTicket().getId();
        commentService.deleteComment(comment);
        return "redirect:/ticketdetails/" + ticket_id;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/deleteuser")
    public String deleteUser(@RequestParam(name = "id") Long id){
        User user = getCurrentUser();
        if (user.getEmail().equals("demoadmin@bugtracker.io") || user.getEmail().equals("demouser@bugtracker.io") ||
                user.getEmail().equals("demodeveloper@bugtracker.io") || user.getEmail().equals("demopm@bugtracker.io")){
            return "redirect:/demoaccounterror";
        }
        userService.deleteUser(id);
        return "redirect:/logout";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PROJECT_MANAGER')")
    @PostMapping("/deleteproject")
    public String deleteProject(@RequestParam(name = "project_id") Long id){
        User user = getCurrentUser();
        if (user.getEmail().equals("demoadmin@bugtracker.io") || user.getEmail().equals("demouser@bugtracker.io") ||
                user.getEmail().equals("demodeveloper@bugtracker.io") || user.getEmail().equals("demopm@bugtracker.io")){
            return "redirect:/demoaccounterror";
        }
        projectService.deleteProject(id);
        return "redirect:/projectsadmin";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/removerole")
    public String removeRole(@RequestParam(name = "user_id") Long user_id,
                             @RequestParam(name = "role_id") Long role_id){
        User user = getCurrentUser();
        if (user.getEmail().equals("demoadmin@bugtracker.io") || user.getEmail().equals("demouser@bugtracker.io") ||
                user.getEmail().equals("demodeveloper@bugtracker.io") || user.getEmail().equals("demopm@bugtracker.io")){
            return "redirect:/demoaccounterror";
        }
        userService.removeRole(user_id, role_id);
        return  "redirect:/userdetails/" + user_id;
    }

    //updating
    @PreAuthorize("hasAnyRole('ROLE_DEVELOPER', 'ROLE_ADMIN', 'ROLE_PROJECT_MANAGER')")
    @PostMapping("/changestatus")
    public String updateStatus(@RequestParam(name = "ticket_id") Long ticket_id,
                               @RequestParam(name = "status") String status){
        User user = getCurrentUser();
        if (user.getEmail().equals("demoadmin@bugtracker.io") || user.getEmail().equals("demouser@bugtracker.io") ||
                user.getEmail().equals("demodeveloper@bugtracker.io") || user.getEmail().equals("demopm@bugtracker.io")){
            return "redirect:/demoaccounterror";
        }
        Ticket ticket = ticketService.getTicketEntity(ticket_id);
        ticketService.changeStatus(ticket, status);
        return "redirect:/ticketdetails/" + ticket_id;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/updatepassword")
    public String updatePassword(@RequestParam(name = "old-password") String oldPassword,
                                 @RequestParam(name = "new-password") String newPassword,
                                 @RequestParam(name = "re-new-password") String reNewPassword){
        User user = getCurrentUser();
        if (user.getEmail().equals("demoadmin@bugtracker.io") || user.getEmail().equals("demouser@bugtracker.io") ||
                user.getEmail().equals("demodeveloper@bugtracker.io") || user.getEmail().equals("demopm@bugtracker.io")){
            return "redirect:/demoaccounterror";
        }
        if(!newPassword.equals(reNewPassword)){
            return "redirect:/profile?errorcode=2";
        }
        String updatePassword = userService.updatePassword(user, oldPassword, newPassword);

        return updatePassword;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/updatecredentials")
    public String updateCredentials(@RequestParam(name = "first-name") String firstName,
                                    @RequestParam(name = "last-name") String lastName){
        User user = getCurrentUser();
        if (user.getEmail().equals("demoadmin@bugtracker.io") || user.getEmail().equals("demouser@bugtracker.io") ||
                user.getEmail().equals("demodeveloper@bugtracker.io") || user.getEmail().equals("demopm@bugtracker.io")){
            return "redirect:/demoaccounterror";
        }
        userService.updateCredentials(user, firstName, lastName);
        return "redirect:/profile";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/updateproject")
    public String updateProject(@RequestParam(name = "project_id") Long project_id,
                                @RequestParam(name = "title") String title,
                                @RequestParam(name = "content") String content,
                                @RequestParam(name = "author") Long author_id){
        User currentUser = getCurrentUser();
        User user = userService.getUser(author_id);
        if (currentUser.getEmail().equals("demoadmin@bugtracker.io") || currentUser.getEmail().equals("demouser@bugtracker.io") ||
                currentUser.getEmail().equals("demodeveloper@bugtracker.io") || currentUser.getEmail().equals("demopm@bugtracker.io")){
            return "redirect:/demoaccounterror";
        }
        String update = projectService.updateProject(title, content, user, project_id);

        return "redirect:/editproject/" + project_id + update;
    }

    //upload picture
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/uploadphoto")
    public String uploadPhoto(@RequestParam(name = "profile_pic") MultipartFile file){
        User user = getCurrentUser();
        if (user.getEmail().equals("demoadmin@bugtracker.io") || user.getEmail().equals("demouser@bugtracker.io") ||
                user.getEmail().equals("demodeveloper@bugtracker.io") || user.getEmail().equals("demopm@bugtracker.io")){
            return "redirect:/demoaccounterror";
        }
        if(file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/png")){
            fileUploadService.uploadProfilePic(file, getCurrentUser());
        }
        return "redirect:/profile";
    }

    //view picture
    @GetMapping(value = "/profilepics/{url}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] profilepic(@PathVariable(name = "url") String url) throws IOException{

        String picURL = fileUploadURL + "user.png";
        if(url != null){
            picURL = fileUploadURL + url + ".jpg";
        }
        InputStream in;
        try{
            ClassPathResource resource = new ClassPathResource(picURL);
            in = resource.getInputStream();
        }catch (Exception e){
            picURL = fileUploadURL + "user.png";
            ClassPathResource resource = new ClassPathResource(picURL);
            in = resource.getInputStream();
        }

        return IOUtils.toByteArray(in);
    }


    //Error handling
    @GetMapping("/error")
    public String error(){
        return "error";
    }

    @GetMapping("/demoaccounterror")
    public String demoAccountError(){
        return "demoaccounterror";
    }


    //Getting current User
    private User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            User user = (User) authentication.getPrincipal();
            return user;
        }
        return null;
    }


    //Testing
    @GetMapping("/profilenew")
    public String profileNew(Model model){
        model.addAttribute("user_id", getCurrentUser().getId());
        model.addAttribute("user", getCurrentUser());
        return "profilenew";
    }


}
