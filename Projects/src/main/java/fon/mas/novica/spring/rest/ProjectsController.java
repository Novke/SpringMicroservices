package fon.mas.novica.spring.rest;

import fon.mas.novica.spring.model.dto.project.CreateProjectCmd;
import fon.mas.novica.spring.model.dto.project.ProjectDetails;
import fon.mas.novica.spring.model.dto.project.ProjectInfo;
import fon.mas.novica.spring.model.dto.task.CreateTaskCmd;
import fon.mas.novica.spring.model.dto.task.TaskInfo;
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
    public ResponseEntity<ProjectInfo> createProject(@RequestBody CreateProjectCmd cmd){
        return ResponseEntity.status(HttpStatus.CREATED).body(projectsService.createBlankProject(cmd));
    }
    @GetMapping
    public ResponseEntity<List<ProjectInfo>> getActiveProjects(){
        return ResponseEntity.ok(projectsService.findActiveProjects());
    }
    @GetMapping("/all")
    public ResponseEntity<List<ProjectInfo>> getAllProjects(){
        return ResponseEntity.ok(projectsService.findAllProjects());
    }

    @PostMapping("/{id}")
    public ResponseEntity<TaskInfo> addTask(@PathVariable Long id, @RequestBody CreateTaskCmd cmd){
        return ResponseEntity.ok(projectsService.addTask(id, cmd));
    }

    @GetMapping("/{id}/info")
    public ResponseEntity<ProjectDetails> showProjectDetails(@PathVariable Long id){
        return ResponseEntity.ok(projectsService.showProjectDetails(id));
    }
}
