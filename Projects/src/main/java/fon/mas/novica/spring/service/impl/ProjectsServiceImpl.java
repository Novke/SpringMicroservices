package fon.mas.novica.spring.service.impl;

import feign.FeignException;
import fon.mas.novica.spring.exception.UserNotFoundException;
import fon.mas.novica.spring.io.UsersServiceClient;
import fon.mas.novica.spring.model.dto.project.CreateProjectCmd;
import fon.mas.novica.spring.model.dto.project.ProjectDetails;
import fon.mas.novica.spring.model.dto.project.ProjectInfo;
import fon.mas.novica.spring.model.dto.task.CreateTaskCmd;
import fon.mas.novica.spring.model.dto.task.TaskInfo;
import fon.mas.novica.spring.model.dto.users.UserInfo;
import fon.mas.novica.spring.model.entity.ProjectEntity;
import fon.mas.novica.spring.model.entity.TaskEntity;
import fon.mas.novica.spring.model.enums.Status;
import fon.mas.novica.spring.repository.ProjectsRepository;
import fon.mas.novica.spring.repository.TasksRepository;
import fon.mas.novica.spring.service.ProjectsService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectsServiceImpl implements ProjectsService {

    private final ProjectsRepository projectsRepository;
    private final TasksRepository tasksRepository;
    private final UsersServiceClient usersService;
    private final ModelMapper mapper;

    @Override
    public ProjectInfo createBlankProject(CreateProjectCmd cmd) {
        ProjectEntity project = mapper.map(cmd, ProjectEntity.class);
        project.setCreatedDate(LocalDate.now());

        UserInfo supervisor = findUserById(cmd.getSupervisorId());
        project.setSupervisorId(supervisor.getId());

        return mapper.map(projectsRepository.save(project), ProjectInfo.class);
    }

    @Override
    public List<ProjectInfo> findAllProjects() {
        return projectsRepository.findAll()
                .stream()
                .map(p -> mapper.map(p, ProjectInfo.class))
                .toList();
    }

    @Override
    public List<ProjectInfo> findActiveProjects() {
        return projectsRepository.findAllByStatusNot(Status.FINISHED)
                .stream()
                .map(p -> mapper.map(p, ProjectInfo.class))
                .toList();
    }

    @Override
    public TaskInfo addTask(Long id, CreateTaskCmd cmd) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        ProjectEntity project = projectsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project with id " + id + " not found!"));
        UserInfo asignee = findUserById(cmd.getAssigneeId());
        UserInfo supervisor = findUserById(cmd.getSupervisorId());

        TaskEntity task = mapper.map(cmd, TaskEntity.class);

        project.addTask(task);
        task.setCreatedDate(LocalDate.now());
        task.setAssigneeId(asignee.getId());
        task.setSupervisorId(supervisor.getId());

        TaskInfo taskInfo = mapper.map(tasksRepository.save(task), TaskInfo.class);
        taskInfo.setAssigneeName(asignee.getFullName());
        taskInfo.setSupervisorName(supervisor.getFullName());

        return taskInfo;
    }

    @Override
    public ProjectDetails showProjectDetails(Long id) {
        ProjectEntity project = projectsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project with id " + id + " not found!"));

        List<TaskInfo> tasks = new ArrayList<>();
        project.getTasks().forEach(te -> {
                    TaskInfo taskInfo = mapper.map(te, TaskInfo.class);
                    taskInfo.setAssigneeName(getUserFullNameSilently(te.getAssigneeId()));
                    taskInfo.setSupervisorName(getUserFullNameSilently(te.getSupervisorId()));

                    tasks.add(taskInfo);
                }
        );
        ProjectDetails projectDetails = mapper.map(project, ProjectDetails.class);
        projectDetails.setSupervisorName(getUserFullNameSilently(project.getSupervisorId()));
        projectDetails.setTasks(tasks);

        return projectDetails;
    }

    private String getUserFullNameSilently(Long id){
        try {
            return findUserById(id).getFullName();
        } catch (Exception ex){
            return "UNKOWN";
        }
    }

    private UserInfo findUserById(Long id) {
        try {
            return usersService.findUserById(id);
        } catch (FeignException ex) {
            throw new UserNotFoundException(id + "");
        }
    }
}
