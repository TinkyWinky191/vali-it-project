package ee.valitit.project.service;

import ee.valitit.project.domain.Theme;
import ee.valitit.project.domain.Note;
import ee.valitit.project.domain.User;
import ee.valitit.project.exception.CustomException;
import ee.valitit.project.repository.NoteRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class NoteService extends AuditableService<Note> {

    private UserService userService;
    private NoteRepository noteRepository;
    private ThemeService themeService;

    public List<Note> getNotesList() {
        return noteRepository.findAll();
    }

    public List<Note> getNotesListByUser(User user) {
        return noteRepository.findByUser(user);
    }

    public Note getNote(String noteId) throws CustomException {
        Long id = getLong(noteId);
        if (existsById(id)) {
            return noteRepository.findById(id).get();
        }
        return null;
    }

    private Long getLong(String noteId) throws CustomException {
        try {
            return Long.parseLong(noteId);
        } catch (NumberFormatException e) {
            throw new CustomException("Note could be found only by ID. Type ID should be a number!", HttpStatus.BAD_REQUEST);
        }
    }

    public boolean existsById(Long id) throws CustomException {
        if (noteRepository.existsById(id)) {
            return true;
        } else {
            throw new CustomException("Note with id " + id + " does not exist!", HttpStatus.NOT_FOUND);
        }
    }

    public Note getNoteByIdAndUser(User user, String noteId) throws CustomException {
        Long id = getLong(noteId);
        if (existsByIdAndUser(id, user)) {
            return noteRepository.findByIdAndUser(id, user);
        }
        return null;
    }

    public boolean existsByIdAndUser(Long id, User user) throws CustomException {
        if (noteRepository.existsByIdAndUser(id, user)) {
            return true;
        } else {
            throw new CustomException("User with id " + user.getId() + " doesnt have note with id " +
                    id + "!", HttpStatus.NOT_FOUND);
        }
    }

    public void deleteNote(String noteId) throws CustomException {
        Long id = getLong(noteId);
        if (existsById(id)) {
            noteRepository.deleteById(id);
        }
    }

    public void deleteNote(User user, Note note) throws CustomException {
        if (isNotNullAndIdNotNull(note)) {
            deleteNote(note.getId().toString(), user);
        }
    }

    public boolean isNotNullAndIdNotNull(Note note) throws CustomException {
        if (note != null && note.getId() != null) {
            return true;
        } else {
            throw new CustomException("Note and note ID can't be null! Not Updated!", HttpStatus.BAD_REQUEST);
        }
    }

    public void deleteNote(String noteId, User user) throws CustomException {
        Long id = getLong(noteId);
        if (existsById(id) && existsByIdAndUser(id, user)) {
            noteRepository.deleteById(id);
        }
    }

    public Note updateNote(@Valid Note note) throws CustomException {
        return updateNote(note, note.getId().toString());
    }

    public Note updateNote(@Valid Note note, String userId) throws CustomException {
        User user = userService.getUser(userId);
        if (isNotNullAndIdNotNull(note) && existsByIdAndUser(note.getId(), user)) {
            Note tempNote = noteRepository.findById(note.getId()).get();
            if (note.getUser() == null) {
                note.setUser(user);
            }
            if (note.getTheme() == null) {
                note.setTheme(tempNote.getTheme());
            }
            if (note.getContentText() == null) {
                note.setContentText(tempNote.getContentText());
            }
            super.checkCreateData(noteRepository, note);
            return noteRepository.save(note);
        }
        return null;
    }

    public Note createNote(@Valid Note note, String userId) throws CustomException {
        User user = userService.getUser(userId);
        Theme theme = note.getTheme();
        note.setUser(user);
        if (themeService.isNotNullAndIdNotNull(theme) && themeService.existsByIdAndUser(theme.getId(), user)) {
            return noteRepository.save(note);
        }
        return null;
    }

    public Note createNote(@Valid Note note) throws CustomException {
        User user = userService.getUser(note.getUser().getId().toString());
        return createNote(note, user.getId().toString());
    }

}
