package ee.valitit.project.controller;

import ee.valitit.project.domain.User;
import ee.valitit.project.exception.UserErrorException;
import ee.valitit.project.exception.UserException;
import ee.valitit.project.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @ExceptionHandler
    public ResponseEntity<UserException> handleException(UserErrorException exc) {
        UserException response = new UserException();
        response.setMessage(exc.getMessage());
        response.setStatus(exc.getHttpStatus().value());
        response.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<>(response, exc.getHttpStatus());
    }

    @GetMapping({"/", ""})
    public List<User> getUsers() {
        return userService.getUsersList();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@PathVariable String userId) throws UserErrorException {
        Long id;
        try {
            id = Long.parseLong(userId);
        } catch (NumberFormatException e) {
            throw new UserErrorException("Id should be a number!", HttpStatus.BAD_REQUEST);
        }
        ResponseEntity<?> responseEntity;
        if(userService.isUserExistsById(id)) {
            User user = userService.getUser(id);
            responseEntity = new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            throw new UserErrorException("User with id " + userId + " not found!", HttpStatus.NOT_FOUND);
        }
        return responseEntity;
    }

    @DeleteMapping({"/", ""})
    public ResponseEntity<?> deleteCompany(@RequestBody User user) {
        ResponseEntity<?> responseEntity;
        if (user != null && user.getId() != null) {
            if (userService.isUserExistsById(user.getId())) {
                userService.deleteUser(user.getId());
                responseEntity = new ResponseEntity<>("User deleted!", HttpStatus.OK);
            } else {
                responseEntity = new ResponseEntity<>("User with id " + user.getId() + " not found!", HttpStatus.NOT_FOUND);
            }
        } else {
            responseEntity = new ResponseEntity<>("User or id can't be null!", HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    @DeleteMapping({"/{userId}"})
    public ResponseEntity<?> deleteCompanyById(@PathVariable Long userId) {
        ResponseEntity<?> responseEntity;
        if (userId != null && userService.isUserExistsById(userId)) {
          userService.deleteUser(userId);
          responseEntity = new ResponseEntity<>("User deleted!", HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>("User with id " + userId + " not found!", HttpStatus.NOT_FOUND);
        }
        return responseEntity;
    }






}
