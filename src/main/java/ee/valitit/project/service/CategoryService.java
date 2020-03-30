package ee.valitit.project.service;

import ee.valitit.project.domain.Category;
import ee.valitit.project.domain.User;
import ee.valitit.project.exception.CustomException;
import ee.valitit.project.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class CategoryService extends AuditableService<Category> {

    private UserService userService;
    private CategoryRepository categoryRepository;

    public List<Category> getCategoriesList() {
        return categoryRepository.findAll();
    }

    public List<Category> getCategoriesListByUser(User user) {
        return categoryRepository.findByUser(user);
    }

    public Category getCategory(String categoryId) throws CustomException {
        Long id = getLong(categoryId);
        if (existsById(id)) {
            return categoryRepository.findById(id).get();
        }
        return null;
    }

    private Long getLong(String categoryId) throws CustomException {
        try {
            return Long.parseLong(categoryId);
        } catch (NumberFormatException e) {
            throw new CustomException("Category could be found only by ID. Type ID should be a number!", HttpStatus.BAD_REQUEST);
        }
    }

    public boolean existsById(Long id) throws CustomException {
        if (categoryRepository.existsById(id)) {
            return true;
        } else {
            throw new CustomException("Category with id " + id + " does not exist!", HttpStatus.NOT_FOUND);
        }
    }

    public Category getCategoryByIdAndUser(User user, String categoryId) throws CustomException {
        Long id = getLong(categoryId);
        if (existsByIdAndUser(id, user)) {
            return categoryRepository.findByIdAndUser(id, user);
        }
        return null;
    }

    public boolean existsByIdAndUser(Long id, User user) throws CustomException {
        if (categoryRepository.existsByIdAndUser(id, user)) {
            return true;
        } else {
            throw new CustomException("User with id " + user.getId() + " doesnt have category with id " +
                    id + "!", HttpStatus.NOT_FOUND);
        }
    }

    public void deleteCategory(String categoryId) throws CustomException {
        Long id = getLong(categoryId);
        if (existsById(id)) {
            categoryRepository.deleteById(id);
        }
    }

    public void deleteCategory(User user, Category category) throws CustomException {
        if (isNotNullAndIdNotNull(category)) {
            deleteCategory(category.getId().toString(), user);
        }
    }

    public boolean isNotNullAndIdNotNull(Category category) throws CustomException {
        if (category != null && category.getId() != null) {
            return true;
        } else {
            throw new CustomException("Category and category ID can't be null! Not Updated!", HttpStatus.BAD_REQUEST);
        }
    }

    public void deleteCategory(String categoryId, User user) throws CustomException {
        Long id = getLong(categoryId);
        if (existsById(id) && existsByIdAndUser(id, user)) {
            categoryRepository.deleteById(id);
        }
    }

    public void updateCategory(@Valid Category category) throws CustomException {
        updateCategory(category, category.getId().toString());
    }

    public Category updateCategory(@Valid Category category, String userId) throws CustomException {
        User user = userService.getUser(userId);
        if (isNotNullAndIdNotNull(category) && existsByIdAndUser(category.getId(), user)) {
            Category tempCategory = categoryRepository.findById(category.getId()).get();
            if (category.getUser() == null) {
                category.setUser(user);
            }
            if (category.getDescription() == null) {
                category.setDescription(tempCategory.getDescription());
            }
            if (category.getImageUrl() == null) {
                category.setImageUrl(tempCategory.getImageUrl());
            }
            if (category.getThemes() == null || !category.getThemes().isEmpty()) {
                category.setThemes(tempCategory.getThemes());
            }
            super.checkCreateData(categoryRepository, category);
            return categoryRepository.save(category);
        }
        return category;
    }

    public Category createCategory(@Valid Category category, String userId) throws CustomException {
        User user = userService.getUser(userId);
        if (category.getUser() == null) {
            category.setUser(user);
        }
        return categoryRepository.save(category);
    }

    public void createCategory(@Valid Category category) throws CustomException {
        User user = userService.getUser(category.getUser().getId().toString());
        createCategory(category, user.getId().toString());
    }
}
