package fon.mas.novica.spring.notifications.service;

import fon.mas.novica.spring.notifications.model.Contact;
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

    public void notifyAssignment(Contact contact){
        SimpleMailMessage message = new SimpleMailMessage();

        log.info("Contant info recieved: " + contact);

        String subject = String.format("%s, you have been assigned a new task!", contact.getFirstName());
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
                contact.getFirstName(),
                contact.getLastName(),
                contact.getSupervisor(),
                contact.getDueDate(),
                contact.getPriority().toUpperCase(),
                contact.getTaskId(),
                contact.getEmail());


        message.setFrom("donotreply");
        message.setTo(contact.getEmail());
        message.setSubject(subject);
        message.setText(content);

//        mailSender.send(message);
        log.info("Mail sent!");
    }

}
