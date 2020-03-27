package ee.valitit.project.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ThemeDTO {

    private Long id;
    private String name;
    private String description;

}
