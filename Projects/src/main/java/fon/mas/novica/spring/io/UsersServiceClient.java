package fon.mas.novica.spring.io;

import fon.mas.novica.spring.model.dto.users.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "users-ms")
public interface UsersServiceClient {

    @GetMapping("/internal/users/{id}")
    public UserInfo findUserById(@PathVariable Long id);
}
