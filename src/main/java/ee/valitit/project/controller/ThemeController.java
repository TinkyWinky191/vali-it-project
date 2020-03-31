package ee.valitit.project.controller;

import ee.valitit.project.domain.Theme;
import ee.valitit.project.domain.User;
import ee.valitit.project.dto.DefaultResponseDTO;
import ee.valitit.project.exception.CustomException;
import ee.valitit.project.service.ThemeService;
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
public class ThemeController {

    private ThemeService themeService;
    private UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping({"/themes"})
    public ResponseEntity<?> getThemes() {
        return new ResponseEntity<>(themeService.getThemesList(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping({"/themes/{themeId}"})
    public ResponseEntity<?> getTheme(@PathVariable String themeId) throws CustomException {
        return new ResponseEntity<>(themeService.getTheme(themeId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping({"/theme/{themeId}"})
    public ResponseEntity<?> deleteThemeById(@PathVariable String themeId) throws CustomException {
        themeService.deleteTheme(themeId);
        return new ResponseEntity<>(new DefaultResponseDTO("Theme deleted!", HttpStatus.OK.value()), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping({"/themes"})
    public ResponseEntity<?> updateTheme(@RequestBody Theme theme) throws CustomException {
        Theme updatedTheme = themeService.updateTheme(theme);
        return new ResponseEntity<>(updatedTheme, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping({"/themes"})
    public ResponseEntity<?> createTheme(@RequestBody Theme theme) throws CustomException {
        Theme savedTheme = themeService.createTheme(theme);
        return new ResponseEntity<>(savedTheme, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN') or @userService.hasPermissionBySearchingData(#userId, principal.username)")
    @GetMapping({"/users/{userId}/themes"})
    public ResponseEntity<?> getThemesByUserId(@PathVariable String userId) throws CustomException {
        User user = userService.getUser(userId);
        return new ResponseEntity<>(themeService.getThemesListByUser(user), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or @userService.hasPermissionBySearchingData(#userId, principal.username)")
    @GetMapping("/users/{userId}/themes/{themeId}")
    public ResponseEntity<?> getThemeById(@PathVariable String themeId, @PathVariable String userId) throws CustomException {
        User user = userService.getUser(userId);
        return new ResponseEntity<>(themeService.getThemeByIdAndUser(user, themeId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or @userService.hasPermissionBySearchingData(#userId, principal.username)")
    @DeleteMapping({"/users/{userId}/themes"})
    public ResponseEntity<?> deleteTheme(@RequestBody Theme theme, @PathVariable String userId) throws CustomException {
        User user = userService.getUser(userId);
        themeService.deleteTheme(user, theme);
        return new ResponseEntity<>(new DefaultResponseDTO("Theme deleted!", HttpStatus.OK.value()), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or @userService.hasPermissionBySearchingData(#userId, principal.username)")
    @DeleteMapping({"/users/{userId}/themes/{themeId}"})
    public ResponseEntity<?> deleteThemeById(@PathVariable String themeId, @PathVariable String userId) throws CustomException {
        User user = userService.getUser(userId);
        themeService.deleteTheme(themeId, user);
        return new ResponseEntity<>(new DefaultResponseDTO("Theme deleted!", HttpStatus.OK.value()), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or @userService.hasPermissionBySearchingData(#userId, principal.username)")
    @PostMapping({"users/{userId}/themes/"})
    public ResponseEntity<?> createTheme(@RequestBody Theme theme, @PathVariable String userId) throws CustomException {
        Theme createdTheme = themeService.createTheme(theme, userId);
        return new ResponseEntity<>(createdTheme, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN') or @userService.hasPermissionBySearchingData(#userId, principal.username)")
    @PutMapping({"users/{userId}/themes/"})
    public ResponseEntity<?> updateTheme(@RequestBody Theme theme, @PathVariable String userId) throws CustomException {
        Theme updatedTheme = themeService.updateTheme(theme, userId);
        return new ResponseEntity<>(updatedTheme, HttpStatus.ACCEPTED);
    }

}
