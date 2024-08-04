package fon.mas.novica.spring.users.model.dto.user;

import fon.mas.novica.spring.users.model.dto.role.RoleInfo;
import fon.mas.novica.spring.users.model.dto.xp.ExperienceInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInsight {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private RoleInfo role;
    private String email;
    private boolean enabled;
    private ExperienceInfo experience;
}
