package ee.valitit.project.dto;

import lombok.Data;

@Data
public class AuthenticationRequestDTO {

    private String username;
    private String email;
    private String password;

    public String getUsernameOrEmail() {
        if (username == null) {
            return email;
        } else {
            return username;
        }
    }

}
