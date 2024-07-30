package fon.mas.novica.spring.users;

import fon.mas.novica.spring.users.model.entity.RoleEntity;
import fon.mas.novica.spring.users.model.entity.UserEntity;
import fon.mas.novica.spring.users.repository.RolesRepository;
import fon.mas.novica.spring.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
@EnableDiscoveryClient
public class UsersApplication {

    private final RolesRepository rolesRepository;
    private final UsersRepository usersRepository;

    @Value("${config.source}")
    String configSource;

    public static void main(String[] args) {
        SpringApplication.run(UsersApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    void configServerEchoTest(){
        log.info("========== REACHING CONFIG SERVER PROPERTIES ==========");
        if ("Local".equalsIgnoreCase(configSource)) {
            log.warn("Config source is Local!");
        } else {
            log.info("Config source: " + configSource);
        }
        log.info("=======================================================");
    }

    @EventListener(ApplicationReadyEvent.class)
    void databaseInit(){
        if (rolesRepository.count()==0){
            log.debug("Generating roles...");
            rolesRepository.saveAll(List.of(
                    new RoleEntity(1L, "ROLE_USER"),
                    new RoleEntity(2L, "ROLE_ADMIN")
            ));
        }

        if (usersRepository.count()==0){
            log.debug("Generating users...");
            usersRepository.save(
                    new UserEntity(
                            null,
                            "default@email.com",
                            "name",
                            "lastname",
                            "user",
                            encoder().encode("pass"),
                            rolesRepository.findById(2L).orElseThrow(),
                            true));
        }
    }

    @Bean
    ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

}
