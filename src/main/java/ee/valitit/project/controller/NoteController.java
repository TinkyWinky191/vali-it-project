package ee.valitit.project.controller;

import ee.valitit.project.domain.Note;
import ee.valitit.project.exception.CustomException;
import ee.valitit.project.service.NoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/notes")
public class NoteController {

    private NoteService noteService;

    @GetMapping({"/", ""})
    public List<Note> getCategories() {
        return noteService.getNotesList();
    }

    @GetMapping("/{noteId}")
    public ResponseEntity<?> getNote(@PathVariable String noteId) throws CustomException {
        return new ResponseEntity<>(noteService.getNote(noteId), HttpStatus.OK);
    }

    @DeleteMapping({"/", ""})
    public ResponseEntity<?> deleteNote(@RequestBody Note note) throws CustomException {
        noteService.deleteNote(note);
        return new ResponseEntity<>("Note deleted!", HttpStatus.OK);
    }

    @DeleteMapping({"/{noteId}"})
    public ResponseEntity<?> deleteNoteById(@PathVariable String noteId) throws CustomException {
        noteService.deleteNote(noteId);
        return new ResponseEntity<>("Note deleted!", HttpStatus.OK);
    }

    @PostMapping({"/", ""})
    public ResponseEntity<?> createOrUpdateNote(@RequestBody Note note) throws CustomException {
        Long id = note.getId();
        noteService.createOrUpdateNote(note);
        if (id != null) {
            return new ResponseEntity<>("Note updated!", HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("Note created!", HttpStatus.CREATED);
        }
    }
}
