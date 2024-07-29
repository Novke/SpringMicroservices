package fon.mas.novica.spring.users.model.dto.user;

import fon.mas.novica.spring.users.model.dto.role.RoleInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

    private String firstName;
    private String lastName;
    private String username;
    private RoleInfo role;

}
