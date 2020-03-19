package ee.valitit.project.controller;

import ee.valitit.project.domain.Material;
import ee.valitit.project.exception.CustomException;
import ee.valitit.project.service.MaterialService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/materials")
public class MaterialController {

    private MaterialService materialService;

    @GetMapping({"/", ""})
    public List<Material> getCategories() {
        return materialService.getMaterialsList();
    }

    @GetMapping("/{materialId}")
    public ResponseEntity<?> getMaterial(@PathVariable String materialId) throws CustomException {
        return new ResponseEntity<>(materialService.getMaterial(materialId), HttpStatus.OK);
    }

    @DeleteMapping({"/", ""})
    public ResponseEntity<?> deleteMaterial(@RequestBody Material material) throws CustomException {
        materialService.deleteMaterial(material);
        return new ResponseEntity<>("Material deleted!", HttpStatus.OK);
    }

    @DeleteMapping({"/{materialId}"})
    public ResponseEntity<?> deleteMaterialById(@PathVariable String materialId) throws CustomException {
        materialService.deleteMaterial(materialId);
        return new ResponseEntity<>("Material deleted!", HttpStatus.OK);
    }

    @PostMapping({"/", ""})
    public ResponseEntity<?> createOrUpdateMaterial(@RequestBody Material material) throws CustomException {
        Long id = material.getId();
        materialService.createOrUpdateMaterial(material);
        if (id != null) {
            return new ResponseEntity<>("Material updated!", HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("Material created!", HttpStatus.CREATED);
        }
    }
}
