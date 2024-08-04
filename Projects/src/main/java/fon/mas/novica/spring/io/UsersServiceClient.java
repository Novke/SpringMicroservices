package fon.mas.novica.spring.io;

import fon.mas.novica.spring.model.dto.users.UserInfo;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "users-ms", configuration = FeignConfig.class)
public interface UsersServiceClient {

    @GetMapping("/internal/users/{id}")
    @Retry(name = "users-ms")
    UserInfo findUserById(@PathVariable Long id);

    @PostMapping("/internal/users/verify")
    boolean verifyAuthorization(@RequestBody List<Long> ids);

    @PutMapping("internal/users/{id}/add-task")
    int increaseTaskCount(@PathVariable Long id);

}
