package fon.mas.novica.spring.io;

import fon.mas.novica.spring.model.dto.notification.NewAssignmentNotif;
import fon.mas.novica.spring.model.dto.notification.TaskCompletedNotif;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notifications-ms", configuration = FeignConfig.class)
public interface NotificationsServiceClient {

    @PostMapping("/notify/new-assignment")
    @Retry(name="notifications-ms")
    @CircuitBreaker(name = "notifications-ms", fallbackMethod = "notifyAssigneeFallback")
    void notifyAssignee(@RequestBody NewAssignmentNotif notification);

    default void notifyAssigneeFallback(NewAssignmentNotif newAssignmentNotif, Throwable ex){
        Logger log = LoggerFactory.getLogger(this.getClass());
        log.error("NOTIFICATION SENDING FAILED FOR ASSIGNEE {} FOR THE TASK ID: {}",
                newAssignmentNotif.getFirstName() + " " + newAssignmentNotif.getLastName(),
                newAssignmentNotif.getTaskId(),
                ex);
    }

    @PostMapping("/notify/task-completed")
    @Retry(name = "notifications-ms")
    @CircuitBreaker(name = "notifications-ms", fallbackMethod = "notifyTaskCompletedFallback")
    void notifyTaskCompleted(@RequestBody TaskCompletedNotif notification);

    default void notifyTaskCompletedFallback(TaskCompletedNotif notification, Throwable ex){
        Logger log = LoggerFactory.getLogger(this.getClass());
        log.error("NOTIFICATION SENDING FAILED FOR SUPERVISOR {} FOR THE TASK ID: {}",
                notification.getFirstName() + " " + notification.getLastName(),
                notification.getTaskId(),
                ex);
    }
}
