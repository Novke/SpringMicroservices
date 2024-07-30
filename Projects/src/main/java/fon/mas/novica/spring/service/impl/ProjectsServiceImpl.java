package fon.mas.novica.spring.service.impl;

import fon.mas.novica.spring.model.dto.project.CreateProjectCmd;
import fon.mas.novica.spring.model.dto.project.ProjectInfo;
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
    private final ModelMapper mapper;

    @Override
    public ProjectInfo createBlankProject(CreateProjectCmd cmd) {
        ProjectEntity project = mapper.map(cmd, ProjectEntity.class);
        project.setCreatedDate(LocalDate.now());

        //fix
        project.setSupervisorId(1L);

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
