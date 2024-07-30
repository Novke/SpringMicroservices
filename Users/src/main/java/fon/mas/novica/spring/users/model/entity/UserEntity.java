package fon.mas.novica.spring.users.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Email(message = "Invalid email")
    @Column(nullable = false, unique = true)
    private String email;
    private String firstName;
    private String lastName;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @ManyToOne(optional = false)
    private RoleEntity role;
    @Column(columnDefinition = "boolean default true")
    private boolean enabled = true;


}
