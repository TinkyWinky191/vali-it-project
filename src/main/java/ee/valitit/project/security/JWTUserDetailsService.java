package ee.valitit.project.security;

import ee.valitit.project.domain.User;
import ee.valitit.project.repository.UserRepository;
import ee.valitit.project.security.jwt.JWTUserFactory;
import ee.valitit.project.service.UserService;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Data
@Qualifier("jwt")
@Service
public class JWTUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        } else {
            return JWTUserFactory.create(user);
        }
    }

}
