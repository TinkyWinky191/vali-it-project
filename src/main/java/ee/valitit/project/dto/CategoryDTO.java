package ee.valitit.project.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CategoryDTO {

    private Long id;
    private String name;
    private String description;

}
