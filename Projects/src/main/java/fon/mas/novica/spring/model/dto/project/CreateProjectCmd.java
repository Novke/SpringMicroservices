package fon.mas.novica.spring.model.dto.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProjectCmd {

    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate dueDate;
    private Long supervisorId;

}
