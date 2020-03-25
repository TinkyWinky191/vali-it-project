package ee.valitit.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class TokenResponseDTO {

    private Long id;
    private String username;
    private String token;

}
