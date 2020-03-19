package ee.valitit.project.service;

import ee.valitit.project.domain.Role;
import ee.valitit.project.domain.User;
import ee.valitit.project.exception.CustomException;
import ee.valitit.project.repository.RoleRepository;
import ee.valitit.project.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService extends AuditableService<User> {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public List<User> getUsersList() {
        return userRepository.findAll();
    }

    public User register(User user) throws CustomException {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new CustomException("Username is already in use! User not created!", HttpStatus.BAD_REQUEST);
        } else if (userRepository.existsByEmail(user.getEmail())) {
            throw new CustomException("Email is already in use! User not created!", HttpStatus.BAD_REQUEST);
        } else if (user.getProfilePictureUrl() == null || user.getProfilePictureUrl().isEmpty()) {
            user.setProfilePictureUrl("https://pngimage.net/wp-content/uploads/2018/05/default-user-image-png-7.png");
        }
        Role role = roleRepository.findByName("ROLE_USER");
        user.getRoles().add(role);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User getUser(String searchingData) throws CustomException {
        Long id;
        try {
            id = Long.parseLong(searchingData);
        } catch (NumberFormatException e) {
            return getUserByUsernameOrEmail(searchingData);
        }
        if (isUserExistsById(id)) {
            Optional<User> user = userRepository.findById(id);
            return user.get();
        } else {
            throw new CustomException("User with id " + searchingData + " not found!", HttpStatus.NOT_FOUND);
        }
    }

    private User getUserByUsernameOrEmail(String usernameOrEmail) throws CustomException {
        if (usernameOrEmail.contains("@")) {
            if (userRepository.existsByEmail(usernameOrEmail)) {
                return userRepository.findByEmail(usernameOrEmail);
            } else {
                throw new CustomException("User with email: " + usernameOrEmail + " not found!", HttpStatus.BAD_REQUEST);
            }
        } else {
            if (userRepository.existsByUsername(usernameOrEmail)) {
                return userRepository.findByUsername(usernameOrEmail);
            } else {
                throw new CustomException("User with username: " + usernameOrEmail + " not found!", HttpStatus.BAD_REQUEST);
            }
        }
    }

    public boolean isUserExistsById(Long id) {
        return userRepository.existsById(id);
    }

    public void deleteUser(String userId) throws CustomException {
        Long id;
        try {
            id = Long.parseLong(userId);
        } catch (NumberFormatException e) {
            throw new CustomException("Type ID should be a number!", HttpStatus.BAD_REQUEST);
        }
        if (isUserExistsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new CustomException("User with id " + userId + " not found!", HttpStatus.NOT_FOUND);
        }
    }

    public void deleteUser(User user) throws CustomException {
        if (user != null && user.getId() != null) {
            if (isUserExistsById(user.getId())) {
                userRepository.deleteById(user.getId());
            } else {
                throw new CustomException("User with id " + user.getId() + " does not exist!", HttpStatus.NOT_FOUND);
            }
        } else {
            throw new CustomException("User and user ID can't be null!", HttpStatus.BAD_REQUEST);
        }
    }

    public void updateUser(@Valid User user) {
        super.checkCreateData(userRepository, user);
        userRepository.save(user);
    }

}
