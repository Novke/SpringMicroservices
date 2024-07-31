package fon.mas.novica.spring.notifications.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NewAssignmentNotif {

    private String firstName;
    private String lastName;
    private String supervisor;
    private String email;
    private String dueDate;
    private String priority;
    private Long taskId;

}
