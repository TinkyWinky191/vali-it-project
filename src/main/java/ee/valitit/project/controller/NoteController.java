package ee.valitit.project.controller;

import ee.valitit.project.domain.Note;
import ee.valitit.project.domain.User;
import ee.valitit.project.exception.CustomException;
import ee.valitit.project.service.NoteService;
import ee.valitit.project.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class NoteController {

    private NoteService noteService;
    private UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping({"/notes"})
    public ResponseEntity<?> getNotes() {
        return new ResponseEntity<>(noteService.getNotesList(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping({"/notes/{noteId}"})
    public ResponseEntity<?> getNote(@PathVariable String noteId) throws CustomException {
        return new ResponseEntity<>(noteService.getNote(noteId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping({"/note/{noteId}"})
    public ResponseEntity<?> deleteNoteById(@PathVariable String noteId) throws CustomException {
        noteService.deleteNote(noteId);
        return new ResponseEntity<>("Note deleted!", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping({"/notes"})
    public ResponseEntity<?> updateNote(@RequestBody Note note) throws CustomException {
        noteService.updateNote(note);
        return new ResponseEntity<>("Note updated!", HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping({"/notes"})
    public ResponseEntity<?> createNote(@RequestBody Note note) throws CustomException {
        noteService.createNote(note);
        return new ResponseEntity<>("Note created!", HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN') or @userService.hasPermissionBySearchingData(#userId, principal.username)")
    @GetMapping({"/users/{userId}/notes"})
    public ResponseEntity<?> getNotesByUserId(@PathVariable String userId) throws CustomException {
        User user = userService.getUser(userId);
        return new ResponseEntity<>(noteService.getNotesListByUser(user), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or @userService.hasPermissionBySearchingData(#userId, principal.username)")
    @GetMapping("/users/{userId}/notes/{noteId}")
    public ResponseEntity<?> getNoteById(@PathVariable String noteId, @PathVariable String userId) throws CustomException {
        User user = userService.getUser(userId);
        return new ResponseEntity<>(noteService.getNoteByIdAndUser(user, noteId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or @userService.hasPermissionBySearchingData(#userId, principal.username)")
    @DeleteMapping({"/users/{userId}/notes"})
    public ResponseEntity<?> deleteNote(@RequestBody Note note, @PathVariable String userId) throws CustomException {
        User user = userService.getUser(userId);
        noteService.deleteNote(user, note);
        return new ResponseEntity<>("Note deleted!", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or @userService.hasPermissionBySearchingData(#userId, principal.username)")
    @DeleteMapping({"/users/{userId}/notes/{noteId}"})
    public ResponseEntity<?> deleteNoteById(@PathVariable String noteId, @PathVariable String userId) throws CustomException {
        User user = userService.getUser(userId);
        noteService.deleteNote(noteId, user);
        return new ResponseEntity<>("Note deleted!", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or @userService.hasPermissionBySearchingData(#userId, principal.username)")
    @PostMapping({"users/{userId}/notes/"})
    public ResponseEntity<?> createNote(@RequestBody Note note, @PathVariable String userId) throws CustomException {
        noteService.createNote(note, userId);
        return new ResponseEntity<>("Note created!", HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN') or @userService.hasPermissionBySearchingData(#userId, principal.username)")
    @PutMapping({"users/{userId}/notes/"})
    public ResponseEntity<?> updateNote(@RequestBody Note note, @PathVariable String userId) throws CustomException {
        noteService.updateNote(note, userId);
        return new ResponseEntity<>("Note updated!", HttpStatus.ACCEPTED);
    }

}
