package ee.valitit.project.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserDTO {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String profilePictureUrl;
    private String email;
    private boolean gender;

}
