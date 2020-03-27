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
import java.security.Principal;
import java.util.List;

@AllArgsConstructor
@Service
public class UserService extends AuditableService<User> {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public List<User> getUsersList() {
        return userRepository.findAll();
    }

    public User register(@Valid User user) throws CustomException {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new CustomException("Username is already in use! Not created!", HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (userRepository.existsByEmail(user.getEmail())) {
            throw new CustomException("Email is already in use! Not created!", HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (user.getPassword() == null ||
                user.getPassword().isEmpty() ||
                !user.getPassword().matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")) {
            throw new CustomException("Password should contain minimum " +
                    "eight characters, at least one letter and one number!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (user.getProfilePictureUrl() == null || user.getProfilePictureUrl().isEmpty()) {
            user.setProfilePictureUrl("https://pngimage.net/wp-content/uploads/2018/05/default-user-image-png-7.png");
        }
        Role role = roleRepository.findByName("ROLE_USER");
        user.getRoles().add(role);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public void deleteUser(String userId) throws CustomException {
        Long id;
        try {
            id = Long.parseLong(userId);
        } catch (NumberFormatException e) {
            throw new CustomException("Type ID should be a number!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (existsById(id)) {
            userRepository.deleteById(id);
        }
    }

    public boolean existsById(Long id) throws CustomException {
        if (userRepository.existsById(id)) {
            return true;
        } else {
            throw new CustomException("User with id " + id + " does not exist!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void deleteUser(User user) throws CustomException {
        if (isNotNullAndIdNotNull(user) && existsById(user.getId())) {
            userRepository.deleteById(user.getId());
        }
    }

    public boolean isNotNullAndIdNotNull(User user) throws CustomException {
        if (user != null && user.getId() != null) {
            return true;
        } else {
            throw new CustomException("User and user ID can't be null! Not Updated!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public User updateUser(@Valid User user) throws CustomException {
        if (isNotNullAndIdNotNull(user) && existsById(user.getId())) {
            if (user.getPassword() != null) {
                if (user.getPassword().isEmpty() ||
                        !user.getPassword().matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")) {
                    throw new CustomException("Password should contain minimum " +
                            "eight characters, at least one letter and one number!", HttpStatus.INTERNAL_SERVER_ERROR);
                }
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            User tempUser = userRepository.findById(user.getId()).get();
            if (user.getUsername() != null &&
                    userRepository.existsByUsername(user.getUsername()) &&
                    !user.getUsername().equals(tempUser.getUsername())) {
                throw new CustomException("Username is already in use! Not updated!", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if (user.getUsername() != null &&
                    userRepository.existsByEmail(user.getEmail()) &&
                    !user.getEmail().equals(tempUser.getEmail())) {
                throw new CustomException("Email is already in use! Not updated!", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if(user.getUsername() == null) {
                user.setUsername(tempUser.getUsername());
            }
            if(user.getPassword() == null) {
                user.setPassword(tempUser.getPassword());
            }
            if(user.getFirstName() == null) {
                user.setFirstName(tempUser.getFirstName());
            }
            if(user.getLastName() == null) {
                user.setLastName(tempUser.getLastName());
            }
            if(user.getProfilePictureUrl() == null) {
                user.setProfilePictureUrl(tempUser.getProfilePictureUrl());
            }
            if(user.getGender() == null) {
                user.setGender(tempUser.getGender());
            }
            if(user.getCategories() == null || user.getCategories().isEmpty()) {
                user.setCategories(tempUser.getCategories());
            }
            if(user.getThemes() == null || user.getThemes().isEmpty()) {
                user.setThemes(tempUser.getThemes());
            }
            if(user.getNotes() == null || user.getNotes().isEmpty()) {
                user.setNotes(tempUser.getNotes());
            }
            if(user.getNotes() == null || user.getRoles().isEmpty()) {
                user.setRoles(tempUser.getRoles());
            }
        }
        super.checkCreateData(userRepository, user);
       return userRepository.save(user);
    }

    public boolean hasPermissionBySearchingData(String searchingData, String authenticatedUsername) {
        try {
            System.out.println(authenticatedUsername);
            return getUser(searchingData).getUsername().equals(authenticatedUsername);
        } catch (Throwable exc) {
            return false;
        }
    }

    public boolean hasPermissionBySearchingData(String searchingData, Principal principal) {
        try {
            return getUser(searchingData).getUsername().equals(principal.getName());
        } catch (Exception exc) {
            return false;
        }
    }

    public User getUser(String searchingData) throws CustomException {
        Long id;
        try {
            id = Long.parseLong(searchingData);
        } catch (NumberFormatException e) {
            return getUserByUsernameOrEmail(searchingData);
        }
        if (existsById(id)) {
            return userRepository.findById(id).get();
        }
        return null;

    }

    private User getUserByUsernameOrEmail(String usernameOrEmail) throws CustomException {
        if (usernameOrEmail.contains("@")) {
            if (userRepository.existsByEmail(usernameOrEmail)) {
                return userRepository.findByEmail(usernameOrEmail);
            } else {
                throw new CustomException("User with email: " + usernameOrEmail + " not found!", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            if (userRepository.existsByUsername(usernameOrEmail)) {
                return userRepository.findByUsername(usernameOrEmail);
            } else {
                throw new CustomException("User with username: " + usernameOrEmail + " not found!", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

}
