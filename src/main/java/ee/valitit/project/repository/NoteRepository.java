package ee.valitit.project.repository;

import ee.valitit.project.domain.Note;
import ee.valitit.project.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findByUser(User user);
    Note findByIdAndUser(Long id, User user);
    boolean existsByIdAndUser(Long id, User user);

}
