package com.example.backend.config;


import com.example.backend.model.Job;
import com.example.backend.repository.JobRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class JobSeeder implements CommandLineRunner {

    private final JobRepository jobRepo;

    public JobSeeder(JobRepository jobRepo) {
        this.jobRepo = jobRepo;
    }

    @Override
    public void run(String... args) {
        if (jobRepo.count() == 0) {
            Job job1 = new Job("Java Developer", "Build APIs", Arrays.asList("Java", "Spring Boot", "REST"));
            Job job2 = new Job("Frontend Developer", "Angular work", Arrays.asList("Angular", "TypeScript", "HTML"));

            jobRepo.saveAll(Arrays.asList(job1, job2));
            System.out.println("Seeded initial job data.");
        }
    }
}

