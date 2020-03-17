package ee.valitit.project.service;

import ee.valitit.project.domain.User;
import ee.valitit.project.exception.UserException;
import ee.valitit.project.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService {

    private UserRepository userRepository;

    public List<User> getUsersList() {
        return userRepository.findAll();
    }

    public User getUser(String userId) throws UserException {
        Long id;
        try {
            id = Long.parseLong(userId);
        } catch (NumberFormatException e) {
            throw new UserException("User could be found only by ID. Type ID should be a number!", HttpStatus.BAD_REQUEST);
        }
        if(isUserExistsById(id)) {
            Optional<User> user = userRepository.findById(id);
            return user.get();
        } else {
            throw new UserException("User with id " + userId + " not found!", HttpStatus.NOT_FOUND);
        }
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public void deleteUser(User user) throws UserException {
        if (user != null && user.getId() != null) {
            if (isUserExistsById(user.getId())) {
                userRepository.deleteById(user.getId());
            } else {
                throw new UserException("User with id " + user.getId() + " does not exist!", HttpStatus.NOT_FOUND);
            }
        } else {
            throw new UserException("User and user ID can't be null!", HttpStatus.BAD_REQUEST);
        }
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public void createUser(User user) {
        userRepository.save(user);
    }

    public boolean isUserExistsById(Long id) {
        return userRepository.existsById(id);
    }

}
