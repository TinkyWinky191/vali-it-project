package ee.valitit.project.service;

import ee.valitit.project.domain.Note;
import ee.valitit.project.domain.Material;
import ee.valitit.project.exception.CustomException;
import ee.valitit.project.repository.NoteRepository;
import ee.valitit.project.repository.MaterialRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class NoteService extends AuditableService<Note> {

    private NoteRepository noteRepository;
    private MaterialRepository materialRepository;

    public List<Note> getNotesList() {
        return noteRepository.findAll();
    }

    public Note getNote(String noteId) throws CustomException {
        Long id;
        try {
            id = Long.parseLong(noteId);
        } catch (NumberFormatException e) {
            throw new CustomException("Note could be found only by ID. Type ID should be a number!", HttpStatus.BAD_REQUEST);
        }
        if(isNoteExistsById(id)) {
            Optional<Note> note = noteRepository.findById(id);
            return note.get();
        } else {
            throw new CustomException("Note with id " + noteId + " not found!", HttpStatus.NOT_FOUND);
        }
    }

    public void deleteNote(String noteId) throws CustomException {
        Long id;
        try {
            id = Long.parseLong(noteId);
        } catch (NumberFormatException e) {
            throw new CustomException("Type ID should be a number!", HttpStatus.BAD_REQUEST);
        }
        if (isNoteExistsById(id)) {
            noteRepository.deleteById(id);
        } else {
            throw new CustomException("Note with id " + noteId + " not found!", HttpStatus.NOT_FOUND);
        }
    }

    public void deleteNote(Note note) throws CustomException {
        if (note != null && note.getId() != null) {
            if (isNoteExistsById(note.getId())) {
                noteRepository.deleteById(note.getId());
            } else {
                throw new CustomException("Note with id " + note.getId() + " does not exist!", HttpStatus.NOT_FOUND);
            }
        } else {
            throw new CustomException("Note and note ID can't be null!", HttpStatus.BAD_REQUEST);
        }
    }

    public void createOrUpdateNote(@Valid Note note) throws CustomException {
            if (note.getId() != null) {
                if (noteRepository.existsById(note.getId())) {
                    Note tempNote = noteRepository.findById(note.getId()).get();
                    if (note.getMaterial() == null
                            || note.getMaterial().getId() == null
                            || !materialRepository.existsById(note.getMaterial().getId())) {
                        note.setMaterial(tempNote.getMaterial());
                        super.checkCreateData(noteRepository, note);
                        noteRepository.save(note);
                    }
                } else {
                    throw new CustomException("Note with id " + note.getId() +
                            " not found! Note not updated!", HttpStatus.NOT_FOUND);
                }
            } else {
                Material material;
                if (note.getMaterial() != null) {
                    material = note.getMaterial();
                    if (material.getId() != null) {
                        if (materialRepository.existsById(material.getId())) {
                            noteRepository.save(note);
                        } else {
                            throw new CustomException("Cant find material with id: " + material.getId() +
                                    "! Note not created!", HttpStatus.BAD_REQUEST);
                        }
                    } else {
                        throw new CustomException(
                                "Material's id must not be null! Note not created!"
                                , HttpStatus.BAD_REQUEST);
                    }
                } else {
                    throw new CustomException("Material can't be null! Note not created!", HttpStatus.BAD_REQUEST);
                }
            }
    }

    public boolean isNoteExistsById(Long id) {
        return noteRepository.existsById(id);
    }

}
