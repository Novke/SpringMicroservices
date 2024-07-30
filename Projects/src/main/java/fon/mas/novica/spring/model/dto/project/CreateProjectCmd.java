package fon.mas.novica.spring.model.dto.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProjectCmd {

    private String name;
    private String description;
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDate startDate;
    private LocalDate dueDate;
    private Long supervisorId;

}
