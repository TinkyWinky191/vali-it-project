package ee.valitit.project.repository;

import ee.valitit.project.domain.Material;
import ee.valitit.project.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {

    List<Material> findByUser(User user);
    Material findByIdAndUser(Long id, User user);
    boolean existsByIdAndUser(Long id, User user);

}
