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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/auth")
public class AuthenticationController {

    private AuthenticationManager authenticationManager;
    private JWTTokenProvider jwtTokenProvider;
    private UserService userService;

    @PostMapping({"/login"})
    public ResponseEntity<TokenResponseDTO> login(@RequestBody AuthenticationRequestDTO requestDto) throws CustomException {

        try {
            String usernameOrEmail = requestDto.getUsernameOrEmail();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(usernameOrEmail, requestDto.getPassword()));
            User user = userService.getUser(usernameOrEmail);
            String token = jwtTokenProvider.createToken(user.getUsername(), user.getRoles());
            log.info(usernameOrEmail + " is trying to login.");
            return new ResponseEntity<>(new TokenResponseDTO(user.getId(), user.getUsername(), token), HttpStatus.OK);
        } catch (AuthenticationException e) {
            log.warn(e.getMessage());
            throw new CustomException(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping({"/register"})
    public ResponseEntity<?> registerUser(@RequestBody User user) throws CustomException {
        userService.register(user);
        return new ResponseEntity<>("Successfully registered!", HttpStatus.CREATED);
    }
}
