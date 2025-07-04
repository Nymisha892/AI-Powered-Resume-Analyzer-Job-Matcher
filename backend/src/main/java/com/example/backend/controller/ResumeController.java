package com.example.backend.controller;

import com.example.backend.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/resume")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadResume(@RequestParam("file") MultipartFile file) {
        try {
            String filename = resumeService.saveFile(file);
            return ResponseEntity.ok("Resume uploaded successfully: " + filename);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to upload resume: " + e.getMessage());
        }
    }
}