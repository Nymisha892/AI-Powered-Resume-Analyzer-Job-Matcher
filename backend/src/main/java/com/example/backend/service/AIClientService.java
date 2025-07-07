package com.example.backend.service;

import com.example.backend.util.MultipartInputStreamFileResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
public class AIClientService {
        private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Object> analyzeResume(MultipartFile file) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new MultipartInputStreamFileResource(file.getInputStream(), file.getOriginalFilename()));

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);

        String pythonUrl = "http://localhost:8001/analyze-resume";
        ResponseEntity<Map> response = new RestTemplate().postForEntity(pythonUrl, request, Map.class);

        return response.getBody();
    }

}


