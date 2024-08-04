package fon.mas.novica.spring.users.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "experience")
public class ExperienceEntity {

    @Id
    @MapsId
    @OneToOne(optional = false)
    private UserEntity user;

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer count = 0;

    public Integer increase() {
        return ++count;
    }
}
