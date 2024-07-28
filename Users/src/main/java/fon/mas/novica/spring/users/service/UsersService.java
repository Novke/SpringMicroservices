package fon.mas.novica.spring.users.service;

import fon.mas.novica.spring.users.model.dto.user.CreateUserCmd;
import fon.mas.novica.spring.users.model.dto.user.UserInfo;
import fon.mas.novica.spring.users.model.entity.UserEntity;
import fon.mas.novica.spring.users.repository.RolesRepository;
import fon.mas.novica.spring.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;
    private final RolesRepository rolesRepository;
    private final ModelMapper mapper;

    public UserInfo createUser(CreateUserCmd user) {
        UserEntity userRequest = mapper.map(user, UserEntity.class);
        userRequest.setRole(rolesRepository.findById(1L).orElseThrow());
        UserEntity createdUser = usersRepository.save(userRequest);
        return mapper.map(createdUser, UserInfo.class);
    }

    public List<UserInfo> findAllUsers() {
        return usersRepository.findAll().stream()
                .map(u -> mapper.map(u, UserInfo.class))
                .toList();
    }
}
