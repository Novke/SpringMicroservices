package fon.mas.novica.spring.io;

import fon.mas.novica.spring.model.dto.notification.ContactInfo;
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
    void notifyAssignee(@RequestBody ContactInfo contactInfo);

    default void notifyAssigneeFallback(ContactInfo contactInfo, Throwable ex){
        Logger log = LoggerFactory.getLogger(this.getClass());
        log.error("NOTIFICATION SENDING FAILED FOR ASSIGNEE {} FOR THE TASK ID: {}",
                contactInfo.getFirstName() + " " + contactInfo.getLastName(),
                contactInfo.getTaskId(),
                ex);
    }
}
