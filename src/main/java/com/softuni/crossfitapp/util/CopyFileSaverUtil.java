package com.softuni.crossfitapp.util;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.stream.Collectors;

public class CopyFileSaverUtil {

    public static String saveFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        // Generate a unique filename to avoid conflicts
        String filename = file.getOriginalFilename();

        // Construct the full path where you want to save the file
        Path filePath = Paths.get("src/main/resources/static/images").resolve(filename);

        // Open an output stream to the newly created file
        try (OutputStream outputStream = Files.newOutputStream(filePath, StandardOpenOption.CREATE)) {
            // Copy the contents of the uploaded file to the output stream
            IOUtils.copy(file.getInputStream(), outputStream);
        }

        // Return the URL of the saved photo (relative to the application context)
        return "/images/" + filename;
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
