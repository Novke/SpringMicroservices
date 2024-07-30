package fon.mas.novica.spring.users.rest;

import fon.mas.novica.spring.users.model.dto.login.UpdatePasswordCmd;
import fon.mas.novica.spring.users.model.dto.user.CreateUserCmd;
import fon.mas.novica.spring.users.model.dto.user.UserInsight;
import fon.mas.novica.spring.users.model.dto.user.UserInfo;
import fon.mas.novica.spring.users.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @PostMapping
    public ResponseEntity<UserInfo> createUser(@RequestBody CreateUserCmd user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usersService.createUser(user));
    }

    @PostMapping("/admin")
    public ResponseEntity<UserInfo> createAdmin(@RequestBody CreateUserCmd user){
        return ResponseEntity.status(HttpStatus.CREATED).body(usersService.createAdmin(user));
    }

    @GetMapping
    public ResponseEntity<List<UserInfo>> getActiveUsers() {
        return ResponseEntity.ok(usersService.findActiveUsers());
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserInsight>> getAllUsers() {
        return ResponseEntity.ok(usersService.findAllUsers());
    }

    @DeleteMapping("/{user}")
    public ResponseEntity<?> disableUser(@PathVariable String user) {
        usersService.disableUser(user);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PatchMapping("/{user}")
    public ResponseEntity<?> enableUser(@PathVariable String user){
        usersService.enableUser(user);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PutMapping
    public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordCmd cmd){
        usersService.updatePassword(cmd);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

}
