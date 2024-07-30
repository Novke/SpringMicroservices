package fon.mas.novica.spring.repository;

import fon.mas.novica.spring.model.entity.ProjectEntity;
import fon.mas.novica.spring.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectsRepository extends JpaRepository<ProjectEntity, Long> {
    List<ProjectEntity> findAllByStatusNot(Status status);
}
