package fon.mas.novica.spring.users.service.impl;

import fon.mas.novica.spring.users.exception.UserAlreadyDisabledException;
import fon.mas.novica.spring.users.exception.UserAlreadyEnabledException;
import fon.mas.novica.spring.users.model.dto.user.CreateUserCmd;
import fon.mas.novica.spring.users.model.dto.user.UserInfo;
import fon.mas.novica.spring.users.model.entity.UserEntity;
import fon.mas.novica.spring.users.repository.RolesRepository;
import fon.mas.novica.spring.users.repository.UsersRepository;
import fon.mas.novica.spring.users.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UsersServiceImpl implements UsersService {


        private final UsersRepository usersRepository;
        private final RolesRepository rolesRepository;
        private final ModelMapper mapper;
        private final PasswordEncoder passwordEncoder;

        public UserInfo createUser(CreateUserCmd user) {
            UserEntity userRequest = mapper.map(user, UserEntity.class);
            userRequest.setRole(rolesRepository.findById(1L).orElseThrow());
            userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
            UserEntity createdUser = usersRepository.save(userRequest);
            return mapper.map(createdUser, UserInfo.class);
        }

        public List<UserInfo> findAllUsers() {
            return usersRepository.findAll().stream()
                    .map(u -> mapper.map(u, UserInfo.class))
                    .toList();
    }
        public List<UserInfo> findActiveUsers() {
            return usersRepository.findAllByEnabledTrue().stream()
                    .map(u -> mapper.map(u, UserInfo.class))
                    .toList();
    }

    @Override
    public void disableUser(String username) {
            UserEntity user = usersRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException(
                            String.format("User with username %s does not exist", username)));
            if (!user.isEnabled()) throw new UserAlreadyDisabledException();
            user.setEnabled(false);
            usersRepository.save(user);
    }

    @Override
    public void enableUser(String username) {
        UserEntity user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("User with username %s does not exist", username)));
        if (user.isEnabled()) throw new UserAlreadyEnabledException();
        user.setEnabled(true);
        usersRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usersRepository.findByUsername(username).map(
                u -> new User(u.getUsername(),
                        u.getPassword(),
                        u.isEnabled(),
                        true,
                        true,
                        true,
                        List.of(u.getRole())
        )).orElseThrow(
                () -> new UsernameNotFoundException(username));
    }
}
