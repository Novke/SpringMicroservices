package fon.mas.novica.spring.service;

import fon.mas.novica.spring.model.dto.project.CreateProjectCmd;
import fon.mas.novica.spring.model.dto.project.ProjectInfo;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ProjectsService {
    ProjectInfo createBlankProject(CreateProjectCmd project);

    List<ProjectInfo> findAllProjects();

    List<ProjectInfo> findActiveProjects();
}
