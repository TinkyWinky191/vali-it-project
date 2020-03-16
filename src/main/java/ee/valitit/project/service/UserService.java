package ee.valitit.project.service;

import ee.valitit.project.domain.User;
import ee.valitit.project.repository.UserRepository;
import lombok.AllArgsConstructor;
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

    public User getUser(Long id) {
        Optional<User> optUser = userRepository.findById(id);
        return optUser.orElse(null);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
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
