package fon.mas.novica.spring.users.service;

import fon.mas.novica.spring.users.model.dto.login.UpdatePasswordCmd;
import fon.mas.novica.spring.users.model.dto.user.CreateUserCmd;
import fon.mas.novica.spring.users.model.dto.user.UserInsight;
import fon.mas.novica.spring.users.model.dto.user.UserInfo;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UsersService extends UserDetailsService {

    UserInfo createUser(CreateUserCmd user);
    UserInfo createAdmin(CreateUserCmd user);
    List<UserInfo> findActiveUsers();
    List<UserInsight> findAllUsers();
    void disableUser(String username);
    void enableUser(String user);
    void updatePassword(UpdatePasswordCmd cmd);
    UserInsight findById(Long id);
}
