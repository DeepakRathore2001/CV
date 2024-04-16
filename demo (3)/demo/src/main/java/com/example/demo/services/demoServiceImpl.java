package com.example.demo.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.transcribe.AmazonTranscribeClient;
import com.amazonaws.services.transcribe.model.GetTranscriptionJobRequest;
import com.amazonaws.services.transcribe.model.GetTranscriptionJobResult;
import com.amazonaws.services.transcribe.model.StartTranscriptionJobRequest;
import com.amazonaws.services.transcribe.model.StartTranscriptionJobResult;

public class demoServiceImpl {

	
	
	  private final AmazonTranscribeClient transcribeClient;
	    private final S3Service s3Service;

	    @Autowired
	    public demoServiceImpl(AmazonTranscribeClient transcribeClient, S3Service s3Service) {
	        this.transcribeClient = transcribeClient;
	        this.s3Service = s3Service;
	    }

	    public String startTranscription(String fileName, byte[] fileContent) throws IOException {
	        s3Service.uploadFile(fileName, fileContent);
	        StartTranscriptionJobRequest startTranscriptionJobRequest = new StartTranscriptionJobRequest()
	                // Adjust for video format if needed
	                .withMediaFormat("mp3") 
	                .withTranscriptionJobName(fileName)
	                .withLanguageCode("en-US") // Adjust language code as needed
	                .withMediaUri("s3://" + "your-s3-bucket-name" + "/" + fileName);

	        StartTranscriptionJobResult startTranscriptionJobResult = transcribeClient.startTranscriptionJob(startTranscriptionJobRequest);
	        return startTranscriptionJobResult.getTranscriptionJob().getTranscriptionJobName();
	    }

	    public String getTranscriptionResult(String jobName) throws IOException {
	        GetTranscriptionJobResult getTranscriptionJobResult = transcribeClient.getTranscriptionJob(new GetTranscriptionJobRequest().withTranscriptionJobName(jobName));
	        String transcriptFileUri = getTranscriptionJobResult.getTranscriptionJob().getTranscript().getTranscriptFileUri();

	        // Download transcript from S3
	        S3Object object = AmazonS3Client.getObject(new GetObjectRequest("your-s3-bucket-name", transcriptFileUri.split("/")[1]));
	        InputStream transcriptStream = object.getObjectContent();
	        BufferedReader reader = new BufferedReader(new InputStreamReader(transcriptStream));
	        StringBuilder transcriptBuilder = new StringBuilder();
	        String line;
	        while ((line = reader.readLine()) != null) {
	            transcriptBuilder.append(line);
	        }
	        reader.close();
	        return transcriptBuilder.toString();
	    }
}
