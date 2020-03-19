package ee.valitit.project.security.jwt;

import org.springframework.security.core.AuthenticationException;

public class JWTAuthenticationException extends AuthenticationException {

    public JWTAuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }

    public JWTAuthenticationException(String msg) {
        super(msg);
    }

}
