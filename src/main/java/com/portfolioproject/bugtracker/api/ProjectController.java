package com.portfolioproject.bugtracker.api;

import com.portfolioproject.bugtracker.dto.ProjectDTO;
import com.portfolioproject.bugtracker.entities.Project;
import com.portfolioproject.bugtracker.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "api/projects")
@CrossOrigin
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping
    public List<ProjectDTO> getAllProjects(){
        return projectService.getAllProjects();
    }

    @GetMapping(value = "{id}")
    public ProjectDTO getProject(@PathVariable (name = "id") Long id){
        return projectService.getProject(id);
    }

}
