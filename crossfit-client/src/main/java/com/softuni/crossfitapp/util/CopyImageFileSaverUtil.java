package com.softuni.crossfitapp.util;

import com.softuni.crossfitapp.exceptions.FileStorageException;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;
import java.util.stream.Collectors;

public class CopyImageFileSaverUtil {

    public static String saveFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        // Generate a unique filename to avoid conflicts
        String originalFilename = file.getOriginalFilename();
        String uniqueFilename = UUID.randomUUID().toString() + "." + originalFilename;

        // Construct the full path where you want to save the file
        Path filePath = Paths.get("crossfit-client/src/main/resources/static/images").resolve(uniqueFilename);

        // Ensure the parent directory exists
        Files.createDirectories(filePath.getParent());

        // Open an output stream to the newly created file
        try (OutputStream outputStream = Files.newOutputStream(filePath, StandardOpenOption.CREATE_NEW)) {
            // Copy the contents of the uploaded file to the output stream
            IOUtils.copy(file.getInputStream(), outputStream);
        }catch (IOException e){
            throw new FileStorageException("Failed to save file: " + originalFilename, e);
        }

        // Return the URL of the saved photo (relative to the application context)
        return "images/" + uniqueFilename;
    }

    private String extractCoordinates(MultipartFile gpxCoordinates) throws IOException {
        if (gpxCoordinates == null || gpxCoordinates.isEmpty()) {
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(gpxCoordinates.getInputStream(), StandardCharsets.UTF_8))) {
            // Read the contents of the GPX file and convert it to plain text
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }


}
