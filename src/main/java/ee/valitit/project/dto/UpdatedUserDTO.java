package ee.valitit.project.dto;

import ee.valitit.project.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UpdatedUserDTO {

    private User user;
    private String token;

}
