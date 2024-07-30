package fon.mas.novica.spring.service;

import fon.mas.novica.spring.model.dto.project.CreateProjectCmd;
import fon.mas.novica.spring.model.dto.project.ProjectInfo;

import java.util.List;

public interface ProjectsService {
    ProjectInfo createBlankProject(CreateProjectCmd project);

    List<ProjectInfo> findAllProjects();

    List<ProjectInfo> findActiveProjects();
}
