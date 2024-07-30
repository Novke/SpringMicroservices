package fon.mas.novica.spring.users.repository;

import fon.mas.novica.spring.users.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<UserEntity, Long> {

    List<UserEntity> findAllByEnabledTrue();
    Optional<UserEntity> findByUsername(String username);
    @Query("SELECT u.id FROM UserEntity u WHERE u.username = :username")
    Long findIdByUsername(String username);

}
