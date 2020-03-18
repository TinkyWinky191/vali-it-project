package ee.valitit.project.service;

import ee.valitit.project.domain.Category;
import ee.valitit.project.domain.User;
import ee.valitit.project.exception.CustomException;
import ee.valitit.project.repository.CategoryRepository;
import ee.valitit.project.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class CategoryService {

    private CategoryRepository categoryRepository;
    private UserRepository userRepository;

    public List<Category> getCategoriesList() {
        return categoryRepository.findAll();
    }

    public Category getCategory(String categoryId) throws CustomException {
        Long id;
        try {
            id = Long.parseLong(categoryId);
        } catch (NumberFormatException e) {
            throw new CustomException("Category could be found only by ID. Type ID should be a number!", HttpStatus.BAD_REQUEST);
        }
        if(isCategoryExistsById(id)) {
            Optional<Category> category = categoryRepository.findById(id);
            return category.get();
        } else {
            throw new CustomException("Category with id " + categoryId + " not found!", HttpStatus.NOT_FOUND);
        }
    }

    public void deleteCategory(String categoryId) throws CustomException {
        Long id;
        try {
            id = Long.parseLong(categoryId);
        } catch (NumberFormatException e) {
            throw new CustomException("Type ID should be a number!", HttpStatus.BAD_REQUEST);
        }
        if (isCategoryExistsById(id)) {
            categoryRepository.deleteById(id);
        } else {
            throw new CustomException("Category with id " + categoryId + " not found!", HttpStatus.NOT_FOUND);
        }
    }

    public void deleteCategory(Category category) throws CustomException {
        if (category != null && category.getId() != null) {
            if (isCategoryExistsById(category.getId())) {
                categoryRepository.deleteById(category.getId());
            } else {
                throw new CustomException("Category with id " + category.getId() + " does not exist!", HttpStatus.NOT_FOUND);
            }
        } else {
            throw new CustomException("Category and category ID can't be null!", HttpStatus.BAD_REQUEST);
        }
    }

    public void createOrUpdateCategory(@Valid Category category) throws CustomException {
            if (category.getId() != null) {
                if (categoryRepository.existsById(category.getId())) {
                    Category tempCategory = categoryRepository.findById(category.getId()).get();
                    if (category.getUser() == null
                            || category.getUser().getId() == null
                            || !userRepository.existsById(category.getUser().getId())) {
                        category.setUser(tempCategory.getUser());
                        categoryRepository.save(category);
                    }
                } else {
                    throw new CustomException("Category with id " + category.getId() +
                            " not found! Category not updated!", HttpStatus.NOT_FOUND);
                }
            } else {
                User user;
                if (category.getUser() != null) {
                    user = category.getUser();
                    if (user.getId() != null) {
                        if (userRepository.existsById(user.getId())) {
                            categoryRepository.save(category);
                        } else {
                            throw new CustomException("Cant find user with id: " + user.getId() +
                                    "! Category not created!", HttpStatus.BAD_REQUEST);
                        }
                    } else {
                        throw new CustomException(
                                "User's id must not be null! Category not created!"
                                , HttpStatus.BAD_REQUEST);
                    }
                } else {
                    throw new CustomException("User can't be null! Category not created!", HttpStatus.BAD_REQUEST);
                }
            }
    }

    public boolean isCategoryExistsById(Long id) {
        return categoryRepository.existsById(id);
    }

}
