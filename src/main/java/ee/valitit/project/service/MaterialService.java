package ee.valitit.project.service;

import ee.valitit.project.domain.Theme;
import ee.valitit.project.domain.Material;
import ee.valitit.project.exception.CustomException;
import ee.valitit.project.repository.ThemeRepository;
import ee.valitit.project.repository.MaterialRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class MaterialService extends AuditableService<Material> {

    private MaterialRepository materialRepository;
    private ThemeRepository themeRepository;

    public List<Material> getMaterialsList() {
        return materialRepository.findAll();
    }

    public Material getMaterial(String materialId) throws CustomException {
        Long id;
        try {
            id = Long.parseLong(materialId);
        } catch (NumberFormatException e) {
            throw new CustomException("Material could be found only by ID. Type ID should be a number!", HttpStatus.BAD_REQUEST);
        }
        if(isMaterialExistsById(id)) {
            Optional<Material> material = materialRepository.findById(id);
            return material.get();
        } else {
            throw new CustomException("Material with id " + materialId + " not found!", HttpStatus.NOT_FOUND);
        }
    }

    public void deleteMaterial(String materialId) throws CustomException {
        Long id;
        try {
            id = Long.parseLong(materialId);
        } catch (NumberFormatException e) {
            throw new CustomException("Type ID should be a number!", HttpStatus.BAD_REQUEST);
        }
        if (isMaterialExistsById(id)) {
            materialRepository.deleteById(id);
        } else {
            throw new CustomException("Material with id " + materialId + " not found!", HttpStatus.NOT_FOUND);
        }
    }

    public void deleteMaterial(Material material) throws CustomException {
        if (material != null && material.getId() != null) {
            if (isMaterialExistsById(material.getId())) {
                materialRepository.deleteById(material.getId());
            } else {
                throw new CustomException("Material with id " + material.getId() + " does not exist!", HttpStatus.NOT_FOUND);
            }
        } else {
            throw new CustomException("Material and material ID can't be null!", HttpStatus.BAD_REQUEST);
        }
    }

    public void createOrUpdateMaterial(@Valid Material material) throws CustomException {
            if (material.getId() != null) {
                if (materialRepository.existsById(material.getId())) {
                    Material tempMaterial = materialRepository.findById(material.getId()).get();
                    if (material.getTheme() == null
                            || material.getTheme().getId() == null
                            || !themeRepository.existsById(material.getTheme().getId())) {
                        material.setTheme(tempMaterial.getTheme());
                        super.checkCreateData(materialRepository, material);
                        materialRepository.save(material);
                    }
                } else {
                    throw new CustomException("Material with id " + material.getId() +
                            " not found! Material not updated!", HttpStatus.NOT_FOUND);
                }
            } else {
                Theme theme;
                if (material.getTheme() != null) {
                    theme = material.getTheme();
                    if (theme.getId() != null) {
                        if (themeRepository.existsById(theme.getId())) {
                            materialRepository.save(material);
                        } else {
                            throw new CustomException("Cant find theme with id: " + theme.getId() +
                                    "! Material not created!", HttpStatus.BAD_REQUEST);
                        }
                    } else {
                        throw new CustomException(
                                "Theme's id must not be null! Material not created!"
                                , HttpStatus.BAD_REQUEST);
                    }
                } else {
                    throw new CustomException("Theme can't be null! Material not created!", HttpStatus.BAD_REQUEST);
                }
            }
    }

    public boolean isMaterialExistsById(Long id) {
        return materialRepository.existsById(id);
    }

}
