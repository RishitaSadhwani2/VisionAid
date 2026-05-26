package com.visionaid.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.polly.PollyClient;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.sns.SnsClient;

@Configuration
public class AwsClientsConfig {

    private static final String AWS_PROFILE_NAME = "visionaid";

    @Bean
    public Region awsRegion(@Value("${visionaid.aws.region}") String region) {
        return Region.of(region);
    }

    @Bean
    public ProfileCredentialsProvider profileCredentialsProvider() {
        return ProfileCredentialsProvider.create(AWS_PROFILE_NAME);
    }

    @Bean
    public S3Client s3Client(Region region, ProfileCredentialsProvider credentialsProvider) {
        return S3Client.builder()
                .region(region)
                .credentialsProvider(credentialsProvider)
                .build();
    }

    @Bean
    public S3Presigner s3Presigner(Region region, ProfileCredentialsProvider credentialsProvider) {
        return S3Presigner.builder()
                .region(region)
                .credentialsProvider(credentialsProvider)
                .build();
    }

    @Bean
    public RekognitionClient rekognitionClient(Region region, ProfileCredentialsProvider credentialsProvider) {
        return RekognitionClient.builder()
                .region(region)
                .credentialsProvider(credentialsProvider)
                .build();
    }

    @Bean
    public PollyClient pollyClient(Region region, ProfileCredentialsProvider credentialsProvider) {
        return PollyClient.builder()
                .region(region)
                .credentialsProvider(credentialsProvider)
                .build();
    }

    @Bean
    public SnsClient snsClient(Region region, ProfileCredentialsProvider credentialsProvider) {
        return SnsClient.builder()
                .region(region)
                .credentialsProvider(credentialsProvider)
                .build();
    }
}