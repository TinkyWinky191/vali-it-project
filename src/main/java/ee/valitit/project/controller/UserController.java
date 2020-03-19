package ee.valitit.project.controller;

import ee.valitit.project.domain.User;
import ee.valitit.project.exception.CustomException;
import ee.valitit.project.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class    UserController {

    private UserService userService;

    @GetMapping({"/", ""})
    public List<User> getUsers() {
        return userService.getUsersList();
    }

    @GetMapping("/{searchingData}")
    public ResponseEntity<?> getUser(@PathVariable String searchingData) throws CustomException {
        return new ResponseEntity<>(userService.getUser(searchingData), HttpStatus.OK);
    }

    @DeleteMapping({"/", ""})
    public ResponseEntity<?> deleteUser(@RequestBody User user) throws CustomException {
        userService.deleteUser(user);
        return new ResponseEntity<>("User deleted!", HttpStatus.OK);
    }

    @DeleteMapping({"/{userId}"})
    public ResponseEntity<?> deleteUserById(@PathVariable String userId) throws CustomException {
        userService.deleteUser(userId);
        return new ResponseEntity<>("User deleted!", HttpStatus.OK);
    }

    @PutMapping({"", "/"})
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        userService.updateUser(user);
        return new ResponseEntity<>("User updated!", HttpStatus.ACCEPTED);
    }

    @PostMapping({"", "/"})
    public ResponseEntity<?> registerUser(@RequestBody User user) throws CustomException {
        userService.register(user);
        return new ResponseEntity<>("User created!", HttpStatus.CREATED);
    }
}
