package ee.valitit.project.repository;

import ee.valitit.project.domain.Category;
import ee.valitit.project.domain.Theme;
import ee.valitit.project.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByUser(User user);
    Category findByIdAndUser(Long id, User user);
    boolean existsByIdAndUser(Long id, User user);

}
