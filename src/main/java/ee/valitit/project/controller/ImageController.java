package ee.valitit.project.controller;

import ee.valitit.project.domain.Image;
import ee.valitit.project.dto.ImageDTO;
import ee.valitit.project.exception.CustomException;
import ee.valitit.project.service.ImageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Slf4j
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class ImageController {

    private ImageService imageService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/uploadImage")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) throws CustomException {
        Image image = imageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/getImage/")
                .path(image.getId().toString())
                .toUriString();
        ImageDTO imageDTO = new ImageDTO(image.getFileName(), fileDownloadUri,
                file.getContentType(), file.getSize());
        log.info(image.getFileName() + " uploaded.");
        log.info(imageDTO.getImageUrl());
        return new ResponseEntity<>(imageDTO, HttpStatus.OK);
    }

    @GetMapping("/getImage/{imageId}")
    public ResponseEntity<?> downloadFile(@PathVariable String imageId) throws CustomException {
        Image image = imageService.getFile(imageId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getFileName() + "\"")
                .body(new ByteArrayResource(image.getData()));
    }


}
