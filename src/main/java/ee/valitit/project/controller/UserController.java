package ee.valitit.project.controller;

import ee.valitit.project.domain.User;
import ee.valitit.project.dto.UpdatedUserDTO;
import ee.valitit.project.exception.CustomException;
import ee.valitit.project.security.jwt.JWTTokenProvider;
import ee.valitit.project.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@AllArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/users")
public class    UserController {

    private UserService userService;
    private JWTTokenProvider provider;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping({"/"})
    public ResponseEntity<?> getUsers(@AuthenticationPrincipal Principal principal) {
        log.debug("ADMIN " + principal.getName() + " requested all users.");
        return new ResponseEntity<>(userService.getUsersList(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or @userService.hasPermissionBySearchingData(#searchingData, principal.username)")
    @GetMapping("/{searchingData}")
    public ResponseEntity<?> getUser(@PathVariable String searchingData) throws CustomException {
        return new ResponseEntity<>(userService.getUser(searchingData), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or @userService.hasPermissionBySearchingData(#user.id, principal.username)")
    @DeleteMapping({"/"})
    public ResponseEntity<?> deleteUser(@RequestBody User user) throws CustomException {
        userService.deleteUser(user);
        return new ResponseEntity<>("User deleted!", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or @userService.hasPermissionBySearchingData(#userId, #principal)")
    @DeleteMapping({"/{userId}"})
    public ResponseEntity<?> deleteUserById(@PathVariable String userId, @AuthenticationPrincipal Principal principal) throws CustomException {
        userService.deleteUser(userId);
        return new ResponseEntity<>("User deleted!", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or @userService.hasPermissionBySearchingData(#user.id, principal.username)")
    @PutMapping({""})
    public ResponseEntity<?> updateUser(@RequestBody User user) throws CustomException {
        User updatedUser = userService.updateUser(user);
        String token = provider.createToken(updatedUser.getUsername(), updatedUser.getRoles());
        log.info("User with id " + user.getId() + " updated!");
        return new ResponseEntity<>(new UpdatedUserDTO(updatedUser, token), HttpStatus.OK);
    }

}
