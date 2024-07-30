package fon.mas.novica.spring.model.entity;

import fon.mas.novica.spring.model.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "projects")
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(length = 3000)
    private String description;
    private LocalDate startDate;
    private LocalDate dueDate;
    private LocalDate endDate;
    private LocalDate createdDate;
    private LocalDate updatedDate;
    private Status status = Status.CREATED;
    @Column(nullable = false)
    private Long supervisorId;
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<TaskEntity> tasks;

}

