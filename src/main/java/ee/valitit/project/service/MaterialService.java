package ee.valitit.project.service;

import ee.valitit.project.domain.Theme;
import ee.valitit.project.domain.Material;
import ee.valitit.project.domain.User;
import ee.valitit.project.exception.CustomException;
import ee.valitit.project.repository.MaterialRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class MaterialService extends AuditableService<Material> {

    private UserService userService;
    private MaterialRepository materialRepository;
    private ThemeService themeService;

    public List<Material> getMaterialsList() {
        return materialRepository.findAll();
    }

    public List<Material> getMaterialsListByUser(User user) {
        return materialRepository.findByUser(user);
    }

    public Material getMaterial(String materialId) throws CustomException {
        Long id = getLong(materialId);
        if (existsById(id)) {
            return materialRepository.findById(id).get();
        }
        return null;
    }

    private Long getLong(String materialId) throws CustomException {
        try {
            return Long.parseLong(materialId);
        } catch (NumberFormatException e) {
            throw new CustomException("Material could be found only by ID. Type ID should be a number!", HttpStatus.BAD_REQUEST);
        }
    }

    public boolean existsById(Long id) throws CustomException {
        if (materialRepository.existsById(id)) {
            return true;
        } else {
            throw new CustomException("Material with id " + id + " does not exist!", HttpStatus.NOT_FOUND);
        }
    }

    public Material getMaterialByIdAndUser(User user, String materialId) throws CustomException {
        Long id = getLong(materialId);
        if (existsByIdAndUser(id, user)) {
            return materialRepository.findByIdAndUser(id, user);
        }
        return null;
    }

    public boolean existsByIdAndUser(Long id, User user) throws CustomException {
        if (materialRepository.existsByIdAndUser(id, user)) {
            return true;
        } else {
            throw new CustomException("User with id " + user.getId() + " doesnt have material with id " +
                    id + "!", HttpStatus.NOT_FOUND);
        }
    }

    public void deleteMaterial(String materialId) throws CustomException {
        Long id = getLong(materialId);
        if (existsById(id)) {
            materialRepository.deleteById(id);
        }
    }

    public void deleteMaterial(User user, Material material) throws CustomException {
        if (isNotNullAndIdNotNull(material)) {
            deleteMaterial(material.getId().toString(), user);
        }
    }

    public boolean isNotNullAndIdNotNull(Material material) throws CustomException {
        if (material != null && material.getId() != null) {
            return true;
        } else {
            throw new CustomException("Material and material ID can't be null! Not Updated!", HttpStatus.BAD_REQUEST);
        }
    }

    public void deleteMaterial(String materialId, User user) throws CustomException {
        Long id = getLong(materialId);
        if (existsById(id) && existsByIdAndUser(id, user)) {
            materialRepository.deleteById(id);
        }
    }

    public void updateMaterial(@Valid Material material) throws CustomException {
        updateMaterial(material, material.getId().toString());
    }

    public void updateMaterial(@Valid Material material, String userId) throws CustomException {
        User user = userService.getUser(userId);
        if (isNotNullAndIdNotNull(material) && existsByIdAndUser(material.getId(), user)) {
            Material tempMaterial = materialRepository.findById(material.getId()).get();
            if (material.getUser() == null) {
                material.setUser(user);
            }
            if (material.getTheme() == null) {
                material.setTheme(tempMaterial.getTheme());
            }
            if (material.getDescription() == null) {
                material.setDescription(tempMaterial.getDescription());
            }
            if (material.getNotes() == null || material.getNotes().isEmpty()) {
                material.setNotes(tempMaterial.getNotes());
            }
            super.checkCreateData(materialRepository, material);
            materialRepository.save(material);
        }
    }

    public void createMaterial(@Valid Material material, String userId) throws CustomException {
        User user = userService.getUser(userId);
        Theme theme = material.getTheme();
        if (themeService.isNotNullAndIdNotNull(theme) && themeService.existsByIdAndUser(theme.getId(), user)) {
            materialRepository.save(material);
        }
    }

    public void createMaterial(@Valid Material material) throws CustomException {
        User user = userService.getUser(material.getUser().getId().toString());
        createMaterial(material, user.getId().toString());
    }

}
