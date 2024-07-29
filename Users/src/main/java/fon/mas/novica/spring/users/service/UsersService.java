package fon.mas.novica.spring.users.service;

import fon.mas.novica.spring.users.model.dto.user.CreateUserCmd;
import fon.mas.novica.spring.users.model.dto.user.UserInfo;

import java.util.List;

public interface UsersService {


    UserInfo createUser(CreateUserCmd user);

    List<UserInfo> findActiveUsers();
    List<UserInfo> findAllUsers();

    void disableUser(String username);

    void enableUser(String user);
}
