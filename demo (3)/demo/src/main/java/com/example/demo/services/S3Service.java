package com.example.demo.services;

import java.io.ByteArrayInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class S3Service {

    private final AmazonS3Client s3Client;

    @Autowired
    public S3Service(AmazonS3Client s3Client) {
        this.s3Client = s3Client;
    }

    public void uploadFile(String fileName, byte[] fileContent) {
        PutObjectRequest putObjectRequest = new PutObjectRequest("your-s3-bucket-name", fileName, new ByteArrayInputStream(fileContent), null);
        s3Client.putObject(putObjectRequest);
    }
}