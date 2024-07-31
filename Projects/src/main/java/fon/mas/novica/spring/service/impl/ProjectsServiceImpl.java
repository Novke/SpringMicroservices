package fon.mas.novica.spring.service.impl;

import feign.FeignException;
import fon.mas.novica.spring.exception.ProjectNotFoundException;
import fon.mas.novica.spring.exception.TaskNotFoundException;
import fon.mas.novica.spring.exception.UnauthorizedActionException;
import fon.mas.novica.spring.exception.UserNotFoundException;
import fon.mas.novica.spring.io.NotificationsServiceClient;
import fon.mas.novica.spring.io.UsersServiceClient;
import fon.mas.novica.spring.model.dto.notification.NewAssignmentNotif;
import fon.mas.novica.spring.model.dto.notification.TaskCompletedNotif;
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
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectsServiceImpl implements ProjectsService {

    private final ProjectsRepository projectsRepository;
    private final TasksRepository tasksRepository;
    private final UsersServiceClient usersService;
    private final NotificationsServiceClient notificationsService;
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
                .orElseThrow(() -> new ProjectNotFoundException("Project with id " + id + " not found!"));

        throwIfUnauthorized(List.of(project.getSupervisorId()));

        UserInfo assignee = findUserById(cmd.getAssigneeId());
        UserInfo supervisor = findUserById(cmd.getSupervisorId());

        TaskEntity task = mapper.map(cmd, TaskEntity.class);

        project.addTask(task);
        task.setCreatedDate(LocalDate.now());
        task.setAssigneeId(assignee.getId());
        task.setSupervisorId(supervisor.getId());

        TaskInfo taskInfo = mapper.map(tasksRepository.save(task), TaskInfo.class);
        taskInfo.setAssigneeName(assignee.getFullName());
        taskInfo.setSupervisorName(supervisor.getFullName());

        notifyAssignee(assignee, supervisor, taskInfo);

        return taskInfo;
    }

    @Override
    public ProjectDetails showProjectDetails(Long id) {
        ProjectEntity project = projectsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project with id " + id + " not found!"));

        List<TaskInfo> tasks = new ArrayList<>();
        project.getTasks().forEach(te -> {
                    TaskInfo taskInfo = taskEntityToTaskInfo(te);

                    tasks.add(taskInfo);
                }
        );
        ProjectDetails projectDetails = mapper.map(project, ProjectDetails.class);
        projectDetails.setSupervisorName(getUserFullNameSilently(project.getSupervisorId()));
        projectDetails.setTasks(tasks);

        return projectDetails;
    }

    @Override
    public TaskInfo setTaskStatus(Long id, Status status) {
        TaskEntity task = tasksRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(String.format("Task with id %s not found!", id)));

        throwIfUnauthorized(List.of(task.getAssigneeId(), task.getSupervisorId()));

        task.setStatus(status);
        task.setUpdatedDate(LocalDate.now());
        if (status == Status.FINISHED) task.setEndDate(LocalDate.now());

        TaskInfo taskInfo = taskEntityToTaskInfo(tasksRepository.save(task));

        if (status == Status.FINISHED && !Objects.equals(task.getAssigneeId(), task.getSupervisorId())) {
            UserInfo assignee = findUserById(task.getAssigneeId());
            UserInfo supervisor = findUserById(task.getSupervisorId());

            notifyTaskCompleted(assignee, supervisor, taskInfo);
        }

        return taskInfo;
    }

    private void notifyAssignee(UserInfo assignee, UserInfo supervisor, TaskInfo taskInfo){
        NewAssignmentNotif notification = new NewAssignmentNotif(assignee.getFirstName(),
                assignee.getLastName(),
                supervisor.getFullName(),
                assignee.getEmail(),
                taskInfo.getDueDate(),
                taskInfo.getPriority().name(),
                taskInfo.getId());

        notificationsService.notifyAssignee(notification);
    }

    private void notifyTaskCompleted(UserInfo assignee, UserInfo supervisor, TaskInfo taskInfo){
        TaskCompletedNotif notification = new TaskCompletedNotif(
                supervisor.getFirstName(),
                supervisor.getLastName(),
                taskInfo.getId(),
                taskInfo.getTitle(),
                taskInfo.getPriority().toString(),
                supervisor.getEmail(),
                assignee.getFullName(),
                taskInfo.getDueDate());

        notificationsService.notifyTaskCompleted(notification);
    }

    ///////////////////
    //  UTIL FUNKCIJE
    ///////////////////
    private void throwIfUnauthorized(List<Long> ids){
        if (!usersService.verifyAuthorization(ids)) throw new UnauthorizedActionException();
    }

    private TaskInfo taskEntityToTaskInfo(TaskEntity entity){
        TaskInfo taskInfo = mapper.map(entity, TaskInfo.class);
        taskInfo.setAssigneeName(getUserFullNameSilently(entity.getAssigneeId()));
        taskInfo.setSupervisorName(getUserFullNameSilently(entity.getSupervisorId()));

        return taskInfo;
    }

    private String getUserFullNameSilently(Long id){
        try {
            return findUserById(id).getFullName();
        } catch (Exception ignored){
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
