package com.portfolioproject.bugtracker.services;

import com.portfolioproject.bugtracker.dto.ProjectDTO;
import com.portfolioproject.bugtracker.entities.Project;
import com.portfolioproject.bugtracker.entities.Ticket;
import com.portfolioproject.bugtracker.entities.User;
import com.portfolioproject.bugtracker.mappers.ProjectMapper;
import com.portfolioproject.bugtracker.mappers.UserMapper;
import com.portfolioproject.bugtracker.repos.ProjectRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepo projectRepo;

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private TicketService ticketService;

    public List<ProjectDTO> getAllProjects(){
        return projectMapper.toProjectDtoList(projectRepo.findAll());
    }

    public ProjectDTO getProject(Long id){
        return projectMapper.toProjectDto(projectRepo.findById(id).orElseThrow());
    }

    public Project addProject(Project project){
        return projectRepo.save(project);
    }

    public Project getProjectEntity(Long id){
        return projectRepo.findById(id).orElseThrow();
    }

    public void deleteProjectsByUserId(Long id){
        projectRepo.deleteAll(projectRepo.findProjectsByAuthorId(id));
    }

    public long countAllProjects(){
        return projectRepo.count();
    }

    public String updateProject(String title, String content, User author, Long project_id){
        Project project = projectRepo.findById(project_id).orElseThrow();
        project.setName(title);
        project.setContent(content);
        project.setAuthor(author);
        Project updatedProject = projectRepo.save(project);
        if(updatedProject != null){
            return "?updatecode=1";
        }
        else{
            return "?updatecode=2";
        }
    }

    public void deleteProject(Long project_id){
        ticketService.deleteTicketEntitiesByProject(project_id);
        projectRepo.delete(projectRepo.getReferenceById(project_id));
    }

}
