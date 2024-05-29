package com.example.votebe;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

@RestController
public class ImageUploadController {

    @Value("${upload.path}")
    private String uploadPath;

    @PostMapping("/upload")
    public String uploadImage(@RequestParam("image") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return "Failed to upload empty file";
        }
        Path rootLocation = Paths.get(uploadPath);
        Path destinationFile = rootLocation.resolve(
                        Paths.get(file.getOriginalFilename()))
                .normalize().toAbsolutePath();
        if (!destinationFile.getParent().equals(rootLocation.toAbsolutePath())) {
            throw new IllegalStateException("Cannot store file outside current directory.");
        }
        file.transferTo(destinationFile);
        return "File uploaded successfully: /uploads/" + file.getOriginalFilename();
    }
}

