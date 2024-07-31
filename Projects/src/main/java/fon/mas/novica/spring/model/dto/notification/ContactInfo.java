package fon.mas.novica.spring.model.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ContactInfo {


    private String firstName;
    private String lastName;
    private String supervisor;
    private String email;
    private LocalDate dueDate;
    private String priority;
    private Long taskId;

}
