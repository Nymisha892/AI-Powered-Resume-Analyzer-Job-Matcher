package com.example.backend.service;

import com.example.backend.model.Job;
import com.example.backend.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobMatcherService {

    @Autowired
    private JobRepository jobRepo;

    public List<Job> matchJobsBySkills(List<String> extractedSkills) {
        List<Job> allJobs = jobRepo.findAll();

        return allJobs.stream()
                .filter(job -> {
                    long matches = job.getRequiredSkills().stream()
                            .filter(extractedSkills::contains)
                            .count();
                    return matches >= 2; // Adjust threshold
                })
                .collect(Collectors.toList());
    }

}
