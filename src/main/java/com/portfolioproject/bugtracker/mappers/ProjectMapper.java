package com.portfolioproject.bugtracker.mappers;

import com.portfolioproject.bugtracker.dto.ProjectDTO;
import com.portfolioproject.bugtracker.entities.Project;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    ProjectDTO toProjectDto(Project project);
    List<ProjectDTO> toProjectDtoList(List<Project> projectList);

}
