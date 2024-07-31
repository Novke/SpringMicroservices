package fon.mas.novica.spring.notifications.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TaskCompletedNotif {

    private String firstName;
    private String lastName;
    private Long taskId;
    private String taskTitle;
    private String taskPriority;
    private String email;
    private String assigneeName;
    private String dueDate;

}
