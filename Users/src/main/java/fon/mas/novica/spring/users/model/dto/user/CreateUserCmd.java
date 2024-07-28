package fon.mas.novica.spring.users.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserCmd {

    private String username;
    private String password;
    private String firstName;
    private String lastName;

}
