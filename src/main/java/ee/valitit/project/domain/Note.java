package ee.valitit.project.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, exclude = {"user", "theme"})
@Entity
@Table(name = "note")
public class Note extends AuditableEntity{

    @Pattern(regexp = "^.{0,16}$", message = "Title length can be max 16 symbols")
    @NotBlank(message = "Title can't be empty")
    @Column(name = "name")
    private String name;

    @Lob
    @Column(name = "content_text")
    private byte[] contentText;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne(targetEntity = Theme.class)
    @JoinColumn(name = "theme_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private Theme theme;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private User user;

}
