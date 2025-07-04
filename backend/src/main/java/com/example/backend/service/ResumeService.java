package com.example.backend.service;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

@Service
public class ResumeService {

    private final String uploadDir = "uploads/";

    public String saveFile(MultipartFile file) throws IOException {
        if (file.isEmpty() || !file.getOriginalFilename().endsWith(".pdf")) {
            throw new IllegalArgumentException("Only PDF files are allowed.");
        }

        Files.createDirectories(Paths.get(uploadDir));

        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filepath = Paths.get(uploadDir, filename);
        Files.copy(file.getInputStream(), filepath, StandardCopyOption.REPLACE_EXISTING);

        return filename;
    }
}
