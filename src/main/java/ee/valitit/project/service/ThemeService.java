package ee.valitit.project.service;

import ee.valitit.project.domain.Theme;
import ee.valitit.project.domain.Category;
import ee.valitit.project.exception.CustomException;
import ee.valitit.project.repository.ThemeRepository;
import ee.valitit.project.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ThemeService extends AuditableService<Theme> {

    private ThemeRepository themeRepository;
    private CategoryRepository categoryRepository;

    public List<Theme> getThemesList() {
        return themeRepository.findAll();
    }

    public Theme getTheme(String themeId) throws CustomException {
        Long id;
        try {
            id = Long.parseLong(themeId);
        } catch (NumberFormatException e) {
            throw new CustomException("Theme could be found only by ID. Type ID should be a number!", HttpStatus.BAD_REQUEST);
        }
        if(isThemeExistsById(id)) {
            Optional<Theme> theme = themeRepository.findById(id);
            return theme.get();
        } else {
            throw new CustomException("Theme with id " + themeId + " not found!", HttpStatus.NOT_FOUND);
        }
    }

    public void deleteTheme(String themeId) throws CustomException {
        Long id;
        try {
            id = Long.parseLong(themeId);
        } catch (NumberFormatException e) {
            throw new CustomException("Type ID should be a number!", HttpStatus.BAD_REQUEST);
        }
        if (isThemeExistsById(id)) {
            themeRepository.deleteById(id);
        } else {
            throw new CustomException("Theme with id " + themeId + " not found!", HttpStatus.NOT_FOUND);
        }
    }

    public void deleteTheme(Theme theme) throws CustomException {
        if (theme != null && theme.getId() != null) {
            if (isThemeExistsById(theme.getId())) {
                themeRepository.deleteById(theme.getId());
            } else {
                throw new CustomException("Theme with id " + theme.getId() + " does not exist!", HttpStatus.NOT_FOUND);
            }
        } else {
            throw new CustomException("Theme and theme ID can't be null!", HttpStatus.BAD_REQUEST);
        }
    }

    public void createOrUpdateTheme(@Valid Theme theme) throws CustomException {
            if (theme.getId() != null) {
                if (themeRepository.existsById(theme.getId())) {
                    Theme tempTheme = themeRepository.findById(theme.getId()).get();
                    if (theme.getCategory() == null
                            || theme.getCategory().getId() == null
                            || !categoryRepository.existsById(theme.getCategory().getId())) {
                        theme.setCategory(tempTheme.getCategory());
                        super.checkCreateData(themeRepository, theme);
                        themeRepository.save(theme);
                    }
                } else {
                    throw new CustomException("Theme with id " + theme.getId() +
                            " not found! Theme not updated!", HttpStatus.NOT_FOUND);
                }
            } else {
                Category category;
                if (theme.getCategory() != null) {
                    category = theme.getCategory();
                    if (category.getId() != null) {
                        if (categoryRepository.existsById(category.getId())) {
                            themeRepository.save(theme);
                        } else {
                            throw new CustomException("Cant find category with id: " + category.getId() +
                                    "! Theme not created!", HttpStatus.BAD_REQUEST);
                        }
                    } else {
                        throw new CustomException(
                                "Category's id must not be null! Theme not created!"
                                , HttpStatus.BAD_REQUEST);
                    }
                } else {
                    throw new CustomException("Category can't be null! Theme not created!", HttpStatus.BAD_REQUEST);
                }
            }
    }

    public boolean isThemeExistsById(Long id) {
        return themeRepository.existsById(id);
    }

}
