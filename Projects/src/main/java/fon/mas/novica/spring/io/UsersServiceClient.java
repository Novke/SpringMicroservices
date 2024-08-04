package fon.mas.novica.spring.io;

import fon.mas.novica.spring.exception.UsersServiceUnavailableException;
import fon.mas.novica.spring.model.dto.users.UserInfo;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "users-ms", configuration = FeignConfig.class)
public interface UsersServiceClient {

    @GetMapping("/internal/users/{id}")
    @Retry(name = "users-ms", fallbackMethod = "findUserFallback")
    UserInfo findUserById(@PathVariable Long id);
    default UserInfo findUserFallback(Long id, Throwable ex){
        throw new UsersServiceUnavailableException("Can't find user, users service is unreachable!", ex);
    }

    @PostMapping("/internal/users/verify")
    @Retry(name = "users-ms", fallbackMethod = "verifyAuthorizationFallback")
    boolean verifyAuthorization(@RequestBody List<Long> id);
    default boolean verifyAuthorizationFallback(List<Long> ids, Throwable ex){
        throw new UsersServiceUnavailableException("Can't authorize, users service is unreachable!", ex);
    }

    @PutMapping("internal/users/{id}/add-task")
    @Retry(name = "users-ms", fallbackMethod = "increaseTaskCountFallback")
    int increaseTaskCount(@PathVariable Long id);

    default int increaseTaskCountFallback(Long id, Throwable ex){
        throw new UsersServiceUnavailableException("Can't update experience, users service is unreachable!", ex);
    }

}
