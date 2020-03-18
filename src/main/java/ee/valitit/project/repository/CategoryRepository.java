package ee.valitit.project.repository;

import ee.valitit.project.domain.Category;
import ee.valitit.project.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {



}
