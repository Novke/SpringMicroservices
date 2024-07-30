package fon.mas.novica.spring.io;

import fon.mas.novica.spring.model.dto.users.UserInfo;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "users-ms", configuration = FeignConfig.class)
public interface UsersServiceClient {

    @GetMapping("/internal/users/{id}")
    @Retry(name = "users-ms")
    UserInfo findUserById(@PathVariable Long id);
}
