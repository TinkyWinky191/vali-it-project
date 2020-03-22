package ee.valitit.project.service;

import ee.valitit.project.domain.Category;
import ee.valitit.project.domain.Theme;
import ee.valitit.project.domain.User;
import ee.valitit.project.exception.CustomException;
import ee.valitit.project.repository.ThemeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class ThemeService extends AuditableService<Theme> {

    private UserService userService;
    private ThemeRepository themeRepository;
    private CategoryService categoryService;

    public List<Theme> getThemesList() {
        return themeRepository.findAll();
    }

    public List<Theme> getThemesListByUser(User user) {
        return themeRepository.findByUser(user);
    }

    public Theme getTheme(String themeId) throws CustomException {
        Long id = getLong(themeId);
        if (existsById(id)) {
            return themeRepository.findById(id).get();
        }
        return null;
    }

    private Long getLong(String themeId) throws CustomException {
        try {
            return Long.parseLong(themeId);
        } catch (NumberFormatException e) {
            throw new CustomException("Theme could be found only by ID. Type ID should be a number!", HttpStatus.BAD_REQUEST);
        }
    }

    public boolean existsById(Long id) throws CustomException {
        if (themeRepository.existsById(id)) {
            return true;
        } else {
            throw new CustomException("Theme with id " + id + " does not exist!", HttpStatus.NOT_FOUND);
        }
    }

    public Theme getThemeByIdAndUser(User user, String themeId) throws CustomException {
        Long id = getLong(themeId);
        if (existsByIdAndUser(id, user)) {
            return themeRepository.findByIdAndUser(id, user);
        }
        return null;
    }

    public boolean existsByIdAndUser(Long id, User user) throws CustomException {
        if (themeRepository.existsByIdAndUser(id, user)) {
            return true;
        } else {
            throw new CustomException("User with id " + user.getId() + " doesnt have theme with id " +
                    id + "!", HttpStatus.NOT_FOUND);
        }
    }

    public void deleteTheme(String themeId) throws CustomException {
        Long id = getLong(themeId);
        if (existsById(id)) {
            themeRepository.deleteById(id);
        }
    }

    public void deleteTheme(User user, Theme theme) throws CustomException {
        if (isNotNullAndIdNotNull(theme)) {
            deleteTheme(theme.getId().toString(), user);
        }
    }

    public boolean isNotNullAndIdNotNull(Theme theme) throws CustomException {
        if (theme != null && theme.getId() != null) {
            return true;
        } else {
            throw new CustomException("Theme and theme ID can't be null! Not Updated!", HttpStatus.BAD_REQUEST);
        }
    }

    public void deleteTheme(String themeId, User user) throws CustomException {
        Long id = getLong(themeId);
        if (existsById(id) && existsByIdAndUser(id, user)) {
            themeRepository.deleteById(id);
        }
    }

    public void updateTheme(@Valid Theme theme) throws CustomException {
        updateTheme(theme, theme.getId().toString());
    }

    public void updateTheme(@Valid Theme theme, String userId) throws CustomException {
        User user = userService.getUser(userId);
        if (isNotNullAndIdNotNull(theme) && existsByIdAndUser(theme.getId(), user)) {
            Theme tempTheme = themeRepository.findById(theme.getId()).get();
            if (theme.getUser() == null) {
                theme.setUser(user);
            }
            if (theme.getCategory() == null) {
                theme.setCategory(tempTheme.getCategory());
            }
            if (theme.getDescription() == null) {
                theme.setDescription(tempTheme.getDescription());
            }
            if (theme.getMaterials() == null || theme.getMaterials().isEmpty()) {
                theme.setMaterials(tempTheme.getMaterials());
            }
            super.checkCreateData(themeRepository, theme);
            themeRepository.save(theme);
        }
    }

    public void createTheme(@Valid Theme theme, String userId) throws CustomException {
        User user = userService.getUser(userId);
        Category category = theme.getCategory();
        if (categoryService.isNotNullAndIdNotNull(category) && categoryService.existsByIdAndUser(category.getId(), user)) {
            themeRepository.save(theme);
        }
    }

    public void createTheme(@Valid Theme theme) throws CustomException {
        User user = userService.getUser(theme.getUser().getId().toString());
        createTheme(theme, user.getId().toString());
    }

}
