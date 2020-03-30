package ee.valitit.project.controller;

import ee.valitit.project.domain.Category;
import ee.valitit.project.domain.User;
import ee.valitit.project.dto.DefaultResponseDTO;
import ee.valitit.project.exception.CustomException;
import ee.valitit.project.service.CategoryService;
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
public class CategoryController {

    private CategoryService categoryService;
    private UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping({"/categories"})
    public ResponseEntity<?> getCategories() {
        return new ResponseEntity<>(categoryService.getCategoriesList(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping({"/categories/{categoryId}"})
    public ResponseEntity<?> getCategory(@PathVariable String categoryId) throws CustomException {
        return new ResponseEntity<>(categoryService.getCategory(categoryId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping({"/category/{categoryId}"})
    public ResponseEntity<?> deleteCategoryById(@PathVariable String categoryId) throws CustomException {
        categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>("Category deleted!", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping({"/categories"})
    public ResponseEntity<?> updateCategory(@RequestBody Category category) throws CustomException {
        categoryService.updateCategory(category);
        return new ResponseEntity<>("Category updated!", HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping({"/categories"})
    public ResponseEntity<?> createCategory(@RequestBody Category category) throws CustomException {
        categoryService.createCategory(category);
        return new ResponseEntity<>("Category created!", HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN') or @userService.hasPermissionBySearchingData(#userId, principal.username)")
    @GetMapping({"/users/{userId}/categories"})
    public ResponseEntity<?> getCategoriesByUserId(@PathVariable String userId) throws CustomException {
        User user = userService.getUser(userId);
        return new ResponseEntity<>(categoryService.getCategoriesListByUser(user), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or @userService.hasPermissionBySearchingData(#userId, principal.username)")
    @GetMapping("/users/{userId}/categories/{categoryId}")
    public ResponseEntity<?> getCategoryById(@PathVariable String categoryId, @PathVariable String userId) throws CustomException {
        User user = userService.getUser(userId);
        return new ResponseEntity<>(categoryService.getCategoryByIdAndUser(user, categoryId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or @userService.hasPermissionBySearchingData(#userId, principal.username)")
    @DeleteMapping({"/users/{userId}/categories"})
    public ResponseEntity<?> deleteCategory(@RequestBody Category category, @PathVariable String userId) throws CustomException {
        User user = userService.getUser(userId);
        categoryService.deleteCategory(user, category);
        return new ResponseEntity<>(new DefaultResponseDTO("Theme deleted!", HttpStatus.OK.value()), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or @userService.hasPermissionBySearchingData(#userId, principal.username)")
    @DeleteMapping({"/users/{userId}/categories/{categoryId}"})
    public ResponseEntity<?> deleteCategoryById(@PathVariable String categoryId, @PathVariable String userId) throws CustomException {
        User user = userService.getUser(userId);
        categoryService.deleteCategory(categoryId, user);
        return new ResponseEntity<>(new DefaultResponseDTO("Theme deleted!", HttpStatus.OK.value()), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or @userService.hasPermissionBySearchingData(#userId, principal.username)")
    @PostMapping({"users/{userId}/categories/"})
    public ResponseEntity<?> createCategory(@RequestBody Category category, @PathVariable String userId) throws CustomException {
        Category createdCategory = categoryService.createCategory(category, userId);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN') or @userService.hasPermissionBySearchingData(#userId, principal.username)")
    @PutMapping({"users/{userId}/categories/"})
    public ResponseEntity<?> updateCategory(@RequestBody Category category, @PathVariable String userId) throws CustomException {
        Category tempCategory = categoryService.updateCategory(category, userId);
        log.info("Category updated: " + tempCategory);
        return new ResponseEntity<>(tempCategory, HttpStatus.ACCEPTED);
    }

}
