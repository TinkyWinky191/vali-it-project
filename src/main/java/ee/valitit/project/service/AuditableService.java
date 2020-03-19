package ee.valitit.project.service;

import ee.valitit.project.domain.AuditableEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public abstract class AuditableService<U extends AuditableEntity> {

    protected void checkCreateData(JpaRepository<U, Long> repository, U entity) {
        if (entity.getId() != null && entity.getCreatedDate() == null && repository.existsById(entity.getId())) {
            LocalDateTime createdDate = repository.findById(entity.getId()).get().getCreatedDate();
            entity.setCreatedDate(createdDate);
        }
    }

}
