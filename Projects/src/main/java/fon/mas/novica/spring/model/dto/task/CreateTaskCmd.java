package fon.mas.novica.spring.model.dto.task;

import fon.mas.novica.spring.model.entity.TaskEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskCmd {

    private String title;
    private String description;
    private TaskEntity.Priority priority;
    private LocalDate dueDate;
    private LocalDate endDate;
    private Long supervisorId;
    private Long assigneeId;

}
