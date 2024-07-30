package fon.mas.novica.spring.model.dto.project;

import fon.mas.novica.spring.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectInfo {

    private String name;
    private String description;
    private Status status;
    private LocalDate createdDate;
    private LocalDate startDate;
    private LocalDate dueDate;

}
