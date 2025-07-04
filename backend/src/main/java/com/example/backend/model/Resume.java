package com.example.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "resumes")
public class Resume {
    @Id
    private String id;

    private String filename;
    private String userId;
    private Date uploadedAt;
}

