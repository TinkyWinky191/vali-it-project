package ee.valitit.project.controller;

import ee.valitit.project.domain.User;
import ee.valitit.project.dto.AuthenticationRequestDTO;
import ee.valitit.project.dto.TokenResponseDTO;
import ee.valitit.project.exception.CustomException;
import ee.valitit.project.security.jwt.JWTTokenProvider;
import ee.valitit.project.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/auth/")
public class AuthenticationController {

    private AuthenticationManager authenticationManager;
    private JWTTokenProvider jwtTokenProvider;
    private UserService userService;

    @PostMapping({"login/", "login"})
    public ResponseEntity<TokenResponseDTO> login(@RequestBody AuthenticationRequestDTO requestDto) throws CustomException {
        try {
            String username = requestDto.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, requestDto.getPassword()));
            User user = userService.getUser(username);

            String token = jwtTokenProvider.createToken(username, user.getRoles());

            return new ResponseEntity<>(new TokenResponseDTO(username, token), HttpStatus.OK);

        } catch (AuthenticationException e) {
            log.warn(e.getMessage());
            throw new CustomException("Wrong password!", HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping({"register/", "register"})
    public ResponseEntity<?> registerUser(@RequestBody User user) throws CustomException {
        userService.register(user);
        return new ResponseEntity<>("Successfully registered!", HttpStatus.CREATED);
    }
}
