package fon.mas.novica.spring.service;

import fon.mas.novica.spring.model.dto.project.CreateProjectCmd;
import fon.mas.novica.spring.model.dto.project.ProjectDetails;
import fon.mas.novica.spring.model.dto.project.ProjectInfo;
import fon.mas.novica.spring.model.dto.task.CreateTaskCmd;
import fon.mas.novica.spring.model.dto.task.TaskInfo;
import fon.mas.novica.spring.model.enums.Status;

import java.util.List;

public interface ProjectsService {
    ProjectInfo createBlankProject(CreateProjectCmd project);

    List<ProjectInfo> findAllProjects();

    List<ProjectInfo> findActiveProjects();

    TaskInfo addTask(Long id, CreateTaskCmd cmd);

    ProjectDetails showProjectDetails(Long id);

    TaskInfo setTaskStatus(Long id, Status status);
}
