package fon.mas.novica.spring.users.io;

import fon.mas.novica.spring.users.model.dto.user.UserInsight;
import fon.mas.novica.spring.users.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/users")
@RequiredArgsConstructor
public class UsersIO {

    private final UsersService usersService;
    @GetMapping("/{id}")
    public ResponseEntity<UserInsight> getUserById(@PathVariable Long id){
        return ResponseEntity.ok(usersService.findById(id));
    }
//    @GetMapping("/{id}")
//    public ResponseEntity<?> getUser(@PathVariable Long id){
//        return ResponseEntity.ok();
//    }

}
