package ee.valitit.project.service;

import ee.valitit.project.domain.Image;
import ee.valitit.project.exception.CustomException;
import ee.valitit.project.repository.ImageRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@AllArgsConstructor
@Service
public class ImageService {

    private ImageRepository imageRepository;

    public Image storeFile(MultipartFile file) throws CustomException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (fileName.contains("..")) {
                throw new CustomException("Filename contains invalid path sequence " + fileName, HttpStatus.BAD_REQUEST);
            }
            Image tempImage = new Image(fileName, file.getContentType(), file.getBytes());
            return imageRepository.save(tempImage);
        } catch (IOException | CustomException ex) {
            throw new CustomException("Could not store file " + fileName + ". Please try again!", HttpStatus.BAD_REQUEST);
        }
    }

    public Image getFile(String id) throws CustomException {
        Long tempId;
        try {
            tempId = Long.parseLong(id);
        } catch (NumberFormatException exc) {
            throw new CustomException("Id should be a number!", HttpStatus.BAD_REQUEST);
        }
        return imageRepository.findById(tempId)
                .orElseThrow(() -> new CustomException("File not found with id " + id, HttpStatus.NOT_FOUND));
    }

}
