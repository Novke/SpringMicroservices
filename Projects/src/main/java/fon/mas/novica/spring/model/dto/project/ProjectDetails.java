package fon.mas.novica.spring.model.dto.project;

import fon.mas.novica.spring.model.dto.task.TaskInfo;
import fon.mas.novica.spring.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDetails {

    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate dueDate;
    private LocalDate endDate;
    private LocalDate createdDate;
    private Status status;
    private String supervisorName;
    private List<TaskInfo> tasks;

}
