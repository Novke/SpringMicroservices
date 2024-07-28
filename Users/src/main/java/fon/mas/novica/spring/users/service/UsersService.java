package fon.mas.novica.spring.users.service;

import fon.mas.novica.spring.users.model.dto.user.CreateUserCmd;
import fon.mas.novica.spring.users.model.dto.user.UserInfo;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UsersService {


    UserInfo createUser(CreateUserCmd user);

    List<UserInfo> findAllUsers();
}
