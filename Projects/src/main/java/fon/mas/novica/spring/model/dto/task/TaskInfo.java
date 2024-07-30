package fon.mas.novica.spring.model.dto.task;

import fon.mas.novica.spring.model.entity.TaskEntity;
import fon.mas.novica.spring.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskInfo {

    private String title;
    private Status status;
    private TaskEntity.Priority priority;
    private LocalDate dueDate;
    private String assigneeName;
    private String supervisorName;

}
