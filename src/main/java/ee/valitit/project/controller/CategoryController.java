package ee.valitit.project.controller;

import ee.valitit.project.domain.Category;
import ee.valitit.project.exception.CustomException;
import ee.valitit.project.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
public class CategoryController {

    private CategoryService categoryService;

    @GetMapping({"/categories", "/categories/"})
    public List<Category> getCategories() {
        return categoryService.getCategoriesList();
    }

    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<?> getCategory(@PathVariable String categoryId) throws CustomException {
        return new ResponseEntity<>(categoryService.getCategory(categoryId), HttpStatus.OK);
    }

    @DeleteMapping({"/categories/", "/categories"})
    public ResponseEntity<?> deleteCategory(@RequestBody Category category) throws CustomException {
        categoryService.deleteCategory(category);
        return new ResponseEntity<>("Category deleted!", HttpStatus.OK);
    }

    @DeleteMapping({"/categories/{categoryId}"})
    public ResponseEntity<?> deleteCategoryById(@PathVariable String categoryId) throws CustomException {
        categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>("Category deleted!", HttpStatus.OK);
    }

    @PostMapping({"/a"})
    public ResponseEntity<?> createOrUpdateCategory(@RequestBody Category category) throws CustomException {
        Long id = category.getId();
        categoryService.createOrUpdateCategory(category);
        if (id != null) {
            return new ResponseEntity<>("Category updated!", HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("Category created!", HttpStatus.CREATED);
        }
    }
}
