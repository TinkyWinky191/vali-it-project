package ee.valitit.project.controller;

import ee.valitit.project.domain.Theme;
import ee.valitit.project.exception.CustomException;
import ee.valitit.project.service.ThemeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/themes")
public class ThemeController {

    private ThemeService themeService;

    @GetMapping({"/", ""})
    public List<Theme> getCategories() {
        return themeService.getThemesList();
    }

    @GetMapping("/{themeId}")
    public ResponseEntity<?> getTheme(@PathVariable String themeId) throws CustomException {
        return new ResponseEntity<>(themeService.getTheme(themeId), HttpStatus.OK);
    }

    @DeleteMapping({"/", ""})
    public ResponseEntity<?> deleteTheme(@RequestBody Theme theme) throws CustomException {
        themeService.deleteTheme(theme);
        return new ResponseEntity<>("Theme deleted!", HttpStatus.OK);
    }

    @DeleteMapping({"/{themeId}"})
    public ResponseEntity<?> deleteThemeById(@PathVariable String themeId) throws CustomException {
        themeService.deleteTheme(themeId);
        return new ResponseEntity<>("Theme deleted!", HttpStatus.OK);
    }

    @PostMapping({"/", ""})
    public ResponseEntity<?> createOrUpdateTheme(@RequestBody Theme theme) throws CustomException {
        Long id = theme.getId();
        themeService.createOrUpdateTheme(theme);
        if (id != null) {
            return new ResponseEntity<>("Theme updated!", HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("Theme created!", HttpStatus.CREATED);
        }
    }
}
