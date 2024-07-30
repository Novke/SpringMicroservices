package fon.mas.novica.spring.service.impl;

import feign.FeignException;
import fon.mas.novica.spring.exception.UserNotFoundException;
import fon.mas.novica.spring.io.UsersServiceClient;
import fon.mas.novica.spring.model.dto.project.CreateProjectCmd;
import fon.mas.novica.spring.model.dto.project.ProjectInfo;
import fon.mas.novica.spring.model.dto.users.UserInfo;
import fon.mas.novica.spring.model.entity.ProjectEntity;
import fon.mas.novica.spring.model.enums.Status;
import fon.mas.novica.spring.repository.ProjectsRepository;
import fon.mas.novica.spring.service.ProjectsService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectsServiceImpl implements ProjectsService {

    private final ProjectsRepository projectsRepository;
    private final UsersServiceClient usersService;
    private final ModelMapper mapper;

    @Override
    public ProjectInfo createBlankProject(CreateProjectCmd cmd) {
        ProjectEntity project = mapper.map(cmd, ProjectEntity.class);
        project.setCreatedDate(LocalDate.now());

        try {
            UserInfo supervisor = usersService.findUserById(cmd.getSupervisorId());
            project.setSupervisorId(supervisor.getId());
        } catch (FeignException ex){
            throw new UserNotFoundException(cmd.getSupervisorId()+"");
        }

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
}
