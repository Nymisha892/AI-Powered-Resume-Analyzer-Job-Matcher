package com.example.backend.controller;

import com.example.backend.model.Job;
import com.example.backend.model.Resume;
import com.example.backend.repository.ResumeRepository;
import com.example.backend.service.JobMatcherService;
import com.example.backend.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:4200/")
@RequestMapping("/api/resume")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    @Autowired
    private ResumeRepository resumeRepo;

    @Autowired
    private JobMatcherService jobMatcherService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadResume(@RequestParam("file") MultipartFile file, @RequestParam("userid") String userid) {
        try {
            String filename = resumeService.saveFileToDisk(file,userid);
            return ResponseEntity.ok("Resume uploaded successfully: " + filename);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to upload resume: " + e.getMessage());
        }
    }

    @GetMapping("/match-jobs/{resumeId}")
    public ResponseEntity<List<Job>> matchJobs(@PathVariable String resumeId) {
        Resume resume = resumeRepo.findById(resumeId).orElseThrow();
        List<Job> matches = jobMatcherService.matchJobsBySkills(resume.getExtractedSkills());
        return ResponseEntity.ok(matches);
    }

}