package fon.mas.novica.spring.io;

import fon.mas.novica.spring.model.dto.notification.ContactInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notifications-ms", configuration = FeignConfig.class)
public interface NotificationsServiceClient {

    @PostMapping("/notify/new-assignment")
    void notifyAssignee(@RequestBody ContactInfo contactInfo);
}
