package fon.mas.novica.spring.notifications.rest;

import fon.mas.novica.spring.notifications.model.Contact;
import fon.mas.novica.spring.notifications.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notify")
@RequiredArgsConstructor
public class NotificationsController {

    private final EmailService emailService;
    @PostMapping("/new-assignment")
    public ResponseEntity<?> notifyAssignee(@RequestBody Contact contact){
        emailService.notifyAssignment(contact);
        return ResponseEntity.noContent().build();
    }
}
