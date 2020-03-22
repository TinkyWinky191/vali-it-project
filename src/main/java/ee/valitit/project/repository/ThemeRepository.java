package ee.valitit.project.repository;

import ee.valitit.project.domain.Theme;
import ee.valitit.project.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long> {

    List<Theme> findByUser(User user);
    Theme findByIdAndUser(Long id, User user);
    boolean existsByIdAndUser(Long id, User user);

}
