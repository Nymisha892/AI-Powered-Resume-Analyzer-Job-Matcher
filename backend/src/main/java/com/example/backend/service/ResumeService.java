package com.example.backend.service;


import com.example.backend.model.Resume;
import com.example.backend.repository.ResumeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ResumeService {

    private final String uploadDir = "uploads/";

    @Autowired
    private ResumeRepository resumeRepo;

    @Autowired
    private AIClientService aiClientService;


    public String saveFileToDisk(MultipartFile file, String userId) throws IOException {
//        if (file.isEmpty() || !file.getOriginalFilename().endsWith(".pdf")) {
//            throw new IllegalArgumentException("Only PDF files are allowed.");
//        }
//
//        Files.createDirectories(Paths.get(uploadDir));
//
//        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
//        Path filepath = Paths.get(uploadDir, filename);
//        Files.copy(file.getInputStream(), filepath, StandardCopyOption.REPLACE_EXISTING);
//
//        return filename;
        if (file.isEmpty() || !file.getOriginalFilename().endsWith(".pdf")) {
            throw new IllegalArgumentException("Only PDF files are allowed.");
        }

        // Convert to byte[]
        byte[] fileData = file.getBytes();

        // Send file to Python AI Service
        Map<String, Object> aiData = aiClientService.analyzeResume(file);

        List<String> skills = (List<String>) aiData.get("skills");
        String role = (String) aiData.get("suggested_role");

        // Create resume document
        Resume resume = new Resume();
        String originalFilename = file.getOriginalFilename();

        resume.setId(UUID.randomUUID().toString());
        resume.setUserId(userId);
        resume.setFilename(originalFilename);
        resume.setUploadedAt(new Date());
        resume.setExtractedSkills(skills);
        resume.setSuggestedRole(role);
        resume.setFileData(fileData);

        // Save to MongoDB
        resumeRepo.save(resume);

        return originalFilename;
    }

    public String saveResumeAndAnalysis(MultipartFile file, String userId) throws IOException {
        // Save file locally
        String fileName = saveFileToDisk(file,userId);

        // Call Python AI service
        Map<String, Object> aiData = aiClientService.analyzeResume((MultipartFile) new File("uploads/" + fileName));

        // Parse AI response
        List<String> skills = (List<String>) aiData.get("skills");
        String role = (String) aiData.get("suggested_role");

        Resume resume = new Resume();
        resume.setFilename(fileName);
        resume.setUserId(userId);
        resume.setUploadedAt(new Date());
        resume.setExtractedSkills(skills);
        resume.setSuggestedRole(role);

        resumeRepo.save(resume);
        return fileName;
    }
}
