package com.example.demo.controller;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.transcribe.AmazonTranscribe;
import com.amazonaws.services.transcribe.model.LanguageCode;
import com.amazonaws.services.transcribe.model.Media;
import com.amazonaws.services.transcribe.model.StartTranscriptionJobRequest;
import com.example.demo.services.demoServiceImpl;

@RestController
@RequestMapping
public class demoController {

	  @Autowired
	    private AmazonS3 s3Client;

	    @Autowired
	    private AmazonTranscribe amazonTranscribe;

	    @PostMapping("/transcribe")
	    public ResponseEntity<?> transcribe(@RequestParam("file") MultipartFile file) {
	        String fileUrl = uploadFileToS3(file);
	        String jobName = "TranscriptionJob_" + UUID.randomUUID();
	        
	        StartTranscriptionJobRequest request = new StartTranscriptionJobRequest()
	                .withTranscriptionJobName(jobName)
	                .withLanguageCode(LanguageCode.EnUS)
	                .withMediaFormat("mp4")
	                .withMedia(new Media().withMediaFileUri(fileUrl));
	                
	        amazonTranscribe.startTranscriptionJob(request);
	        return ResponseEntity.ok(jobName);
	    }

	    private String uploadFileToS3(MultipartFile file) {
	        String bucketName = "your-bucket-name";
	        String keyName = "uploads/" + UUID.randomUUID() + "_" + file.getOriginalFilename();
	        ObjectMetadata metadata = new ObjectMetadata();
	        metadata.setContentLength(file.getSize());
	        try {
	            s3Client.putObject(new PutObjectRequest(bucketName, keyName, file.getInputStream(), metadata));
	            return s3Client.getUrl(bucketName, keyName).toString();
	        } catch (IOException e) {
	            throw new RuntimeException("Error uploading file to S3", e);
	        }
	    }
}
