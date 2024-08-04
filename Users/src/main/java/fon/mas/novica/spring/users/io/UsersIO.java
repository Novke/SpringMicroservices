package fon.mas.novica.spring.users.io;

import fon.mas.novica.spring.users.model.dto.user.UserInsight;
import fon.mas.novica.spring.users.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/internal/users")
@RequiredArgsConstructor
public class UsersIO {

    private final UsersService usersService;
    @GetMapping("/{id}")
    public ResponseEntity<UserInsight> getUserById(@PathVariable Long id){
        return ResponseEntity.ok(usersService.findById(id));
    }
    @PostMapping("/verify")
    public ResponseEntity<Boolean> getUser(
//            @RequestHeader("Authorization") String jwt,
            Authentication authentication,
            @RequestBody List<Long> ids){

        return ResponseEntity.ok(usersService.checkAuthorization(authentication, ids));
    }

    @PutMapping("/{id}/add-task")
    public ResponseEntity<?> increaseTaskCount(@PathVariable Long id){
        return ResponseEntity.ok(usersService.increaseTaskCount(id));
    }

}
