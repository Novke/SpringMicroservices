package fon.mas.novica.spring.users.model.dto.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswordCmd {
    private String username;
    private String confirmPassword;
    private String newPassword;
}
