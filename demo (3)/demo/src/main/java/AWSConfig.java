import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.transcribe.AmazonTranscribe;
import com.amazonaws.services.transcribe.AmazonTranscribeClientBuilder;

@Configuration


public class AWSConfig {

    @Value("${aws.s3.region}")
    private String region;
    @Value("${aws.access.key.id}")
    private String accessKeyId;

    @Value("${aws.secret.access.key}")
    private String secretAccessKey;


    @Bean
    public AmazonS3 s3client() {
    	 BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKeyId, secretAccessKey);
         return AmazonS3ClientBuilder.standard()
                 .withRegion(region)
                 .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                 .build();    }
    
    @Bean
    public AmazonTranscribe amazonTranscribe() {
        return AmazonTranscribeClientBuilder.standard()
                .withRegion(Regions.fromName(region))
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .build();
    }

  


  
    }

