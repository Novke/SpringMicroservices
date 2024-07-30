package fon.mas.novica.spring.rest;

import fon.mas.novica.spring.model.dto.project.CreateProjectCmd;
import fon.mas.novica.spring.model.dto.project.ProjectInfo;
import fon.mas.novica.spring.service.ProjectsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectsController {

    private final ProjectsService projectsService;

    @PostMapping
    public ResponseEntity<ProjectInfo> createProject(@RequestBody CreateProjectCmd project){
        return ResponseEntity.status(HttpStatus.CREATED).body(projectsService.createBlankProject(project));
    }
    @GetMapping
    public ResponseEntity<List<ProjectInfo>> getActiveProjects(){
        return ResponseEntity.ok(projectsService.findActiveProjects());
    }
    @GetMapping("/all")
    public ResponseEntity<List<ProjectInfo>> getAllProjects(){
        return ResponseEntity.ok(projectsService.findAllProjects());
    }
}
