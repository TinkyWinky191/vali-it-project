package ee.valitit.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class DefaultResponseDTO {

    private String message;
    private int status;

}
