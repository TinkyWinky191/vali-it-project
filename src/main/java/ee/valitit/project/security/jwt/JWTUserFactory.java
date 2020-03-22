package ee.valitit.project.security.jwt;

import ee.valitit.project.domain.Role;
import ee.valitit.project.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class JWTUserFactory {

    public static JWTUser create(User user) {
        return new JWTUser(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getFirstName(),
                user.getLastName(),
                user.getProfilePictureUrl(),
                user.getEmail(),
                user.getGender(),
                getGrantedAuthoritiesFromRoles(user.getRoles())
        );
    }

    public static List<? extends GrantedAuthority> getGrantedAuthoritiesFromRoles(List<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

}
