package ee.valitit.project.controller;

import ee.valitit.project.domain.Material;
import ee.valitit.project.domain.User;
import ee.valitit.project.exception.CustomException;
import ee.valitit.project.service.MaterialService;
import ee.valitit.project.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RestController
public class MaterialController {

    private MaterialService materialService;
    private UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping({"/materials"})
    public ResponseEntity<?> getMaterials() {
        return new ResponseEntity<>(materialService.getMaterialsList(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping({"/materials/{materialId}"})
    public ResponseEntity<?> getMaterial(@PathVariable String materialId) throws CustomException {
        return new ResponseEntity<>(materialService.getMaterial(materialId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping({"/material/{materialId}"})
    public ResponseEntity<?> deleteMaterialById(@PathVariable String materialId) throws CustomException {
        materialService.deleteMaterial(materialId);
        return new ResponseEntity<>("Material deleted!", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping({"/materials"})
    public ResponseEntity<?> updateMaterial(@RequestBody Material material) throws CustomException {
        materialService.updateMaterial(material);
        return new ResponseEntity<>("Material updated!", HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping({"/materials"})
    public ResponseEntity<?> createMaterial(@RequestBody Material material) throws CustomException {
        materialService.createMaterial(material);
        return new ResponseEntity<>("Material created!", HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN') or @userService.hasPermissionBySearchingData(#userId, principal.username)")
    @GetMapping({"/users/{userId}/materials"})
    public ResponseEntity<?> getMaterialsByUserId(@PathVariable String userId) throws CustomException {
        User user = userService.getUser(userId);
        return new ResponseEntity<>(materialService.getMaterialsListByUser(user), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or @userService.hasPermissionBySearchingData(#userId, principal.username)")
    @GetMapping("/users/{userId}/materials/{materialId}")
    public ResponseEntity<?> getMaterialById(@PathVariable String materialId, @PathVariable String userId) throws CustomException {
        User user = userService.getUser(userId);
        return new ResponseEntity<>(materialService.getMaterialByIdAndUser(user, materialId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or @userService.hasPermissionBySearchingData(#userId, principal.username)")
    @DeleteMapping({"/users/{userId}/materials"})
    public ResponseEntity<?> deleteMaterial(@RequestBody Material material, @PathVariable String userId) throws CustomException {
        User user = userService.getUser(userId);
        materialService.deleteMaterial(user, material);
        return new ResponseEntity<>("Material deleted!", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or @userService.hasPermissionBySearchingData(#userId, principal.username)")
    @DeleteMapping({"/users/{userId}/materials/{materialId}"})
    public ResponseEntity<?> deleteMaterialById(@PathVariable String materialId, @PathVariable String userId) throws CustomException {
        User user = userService.getUser(userId);
        materialService.deleteMaterial(materialId, user);
        return new ResponseEntity<>("Material deleted!", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or @userService.hasPermissionBySearchingData(#userId, principal.username)")
    @PostMapping({"users/{userId}/materials/"})
    public ResponseEntity<?> createMaterial(@RequestBody Material material, @PathVariable String userId) throws CustomException {
        materialService.createMaterial(material, userId);
        return new ResponseEntity<>("Material created!", HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN') or @userService.hasPermissionBySearchingData(#userId, principal.username)")
    @PutMapping({"users/{userId}/materials/"})
    public ResponseEntity<?> updateMaterial(@RequestBody Material material, @PathVariable String userId) throws CustomException {
        materialService.updateMaterial(material, userId);
        return new ResponseEntity<>("Material updated!", HttpStatus.ACCEPTED);
    }

}
