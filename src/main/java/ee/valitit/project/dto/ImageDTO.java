package ee.valitit.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ImageDTO {

    private String fileName;
    private String imageUrl;
    private String fileType;
    private long size;

}
