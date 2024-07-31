package fon.mas.novica.spring.notifications.service;

import fon.mas.novica.spring.notifications.model.NewAssignmentNotif;
import fon.mas.novica.spring.notifications.model.TaskCompletedNotif;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void notifyAssignment(NewAssignmentNotif notification){
        SimpleMailMessage message = new SimpleMailMessage();

        log.info("Notification info recieved: " + notification);

        String subject = String.format("%s, you have been assigned a new task!", notification.getFirstName());
        String content = String.format("""
                        New task has been assigned to %s %s by the supervisor %s
                        You are expected to finish the task no later than %s
                        
                        PRIORITY: %s
                        Check the details for the task using id = %s
                        
                        ====================================================================================================================
                        THIS EMAIL AND ANY ATTACHMENTS IS INTENDED SOLELY FOR DESIGNATED RECIPIENTS AND MAY CONTAIN CONFIDENTAL INFORMATION.
                        IF YOU ARE NOT THE INTENDED RECIPIENT (%s) NOTIFY THE ORGANIZATION IMMEDIATELY AND DELETE ALL COPIES.
                        ANY UNAUTHORIZED REVIEW, USE OR DISCLOSURE IS PROHIBITED.
                        ====================================================================================================================
                        """,
                notification.getFirstName(),
                notification.getLastName(),
                notification.getSupervisor(),
                notification.getDueDate(),
                notification.getPriority().toUpperCase(),
                notification.getTaskId(),
                notification.getEmail());


        message.setFrom("donotreply");
        message.setTo(notification.getEmail());
        message.setSubject(subject);
        message.setText(content);

//        mailSender.send(message);
        log.info("Mail sent!");
    }

    public void notifyTaskCompleted(TaskCompletedNotif notification) {
        SimpleMailMessage message = new SimpleMailMessage();

        log.info("Notification info received: " + notification);

        String subject = String.format("Task \"%s\" Completed", notification.getTaskTitle());
        String content = String.format("""
                    Hello %s %s,
                    
                    The task "%s" with ID %d, issued for assignee %s has been marked as completed. As a task supervisor it is in your
                    responsibility to assure the task has been completed properly.
                    
                    PRIORITY: %s
                    Due Date: %s
                    
                    Thank you for completing the task on time!
                    
                    ====================================================================================================================
                    THIS EMAIL AND ANY ATTACHMENTS IS INTENDED SOLELY FOR DESIGNATED RECIPIENTS AND MAY CONTAIN CONFIDENTAL INFORMATION.
                    IF YOU ARE NOT THE INTENDED RECIPIENT (%s) NOTIFY THE ORGANIZATION IMMEDIATELY AND DELETE ALL COPIES.
                    ANY UNAUTHORIZED REVIEW, USE OR DISCLOSURE IS PROHIBITED.
                    ====================================================================================================================
                    """,
                notification.getFirstName(),
                notification.getLastName(),
                notification.getTaskTitle(),
                notification.getTaskId(),
                notification.getAssigneeName(),
                notification.getTaskPriority(),
                notification.getDueDate(),
                notification.getEmail());

        message.setFrom("donotreply");
        message.setTo(notification.getEmail());
        message.setSubject(subject);
        message.setText(content);

//        mailSender.send(message);
        log.info("Mail sent!");
    }

}
