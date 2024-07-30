package fon.mas.novica.spring.model.entity;

import fon.mas.novica.spring.model.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tasks")
public class TaskEntity {

    public enum Priority {
        LOW, NORMAL, HIGH
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private Priority priority;
    private LocalDate dueDate;
    private LocalDate endDate;
    private LocalDate createdDate;
    private LocalDate updatedDate;
    private Long supervisorId;
    private Long assigneeId;
    @Enumerated(EnumType.STRING)
    private Status status = Status.CREATED;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private ProjectEntity project;


}
