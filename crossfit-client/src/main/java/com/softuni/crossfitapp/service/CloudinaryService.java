package com.softuni.crossfitapp.service;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {

     String uploadPhoto(MultipartFile file, String folderName);

    void deletePhoto(String publicId);
}
