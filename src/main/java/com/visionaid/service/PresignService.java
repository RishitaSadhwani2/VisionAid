package com.visionaid.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URL;
import java.time.Duration;
import java.time.Instant;

@Service
public class PresignService {

    private final S3Presigner presigner;
    private final String uploadsBucket;
    private final long expirySeconds;
    private final String userPrefix;

    public PresignService(
            S3Presigner presigner,
            @Value("${visionaid.aws.uploadsBucket}") String uploadsBucket,
            @Value("${visionaid.aws.presignExpirySeconds}") long expirySeconds,
            @Value("${visionaid.app.userPrefix}") String userPrefix
    ) {
        this.presigner = presigner;
        this.uploadsBucket = uploadsBucket;
        this.expirySeconds = expirySeconds;
        this.userPrefix = userPrefix;
    }

    public PresignedPut createPresignedPut(String originalFileName, String contentType) {
        String safeFileName = sanitizeFileName(originalFileName);

        String jobId = "job-" + Instant.now()
                .toString()
                .replace(":", "")
                .replace(".", "");

        String s3Key = "uploads/" + userPrefix + "/" + jobId + "/" + safeFileName;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(uploadsBucket)
                .key(s3Key)
                .contentType(contentType)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofSeconds(expirySeconds))
                .putObjectRequest(putObjectRequest)
                .build();

        URL presignedUrl = presigner.presignPutObject(presignRequest).url();

        return new PresignedPut(s3Key, presignedUrl.toString(), expirySeconds);
    }

    private String sanitizeFileName(String fileName) {
        String cleaned = fileName == null ? "input.jpg" : fileName.trim();

        cleaned = cleaned.replaceAll("[^a-zA-Z0-9._-]", "_");

        if (cleaned.length() > 80) {
            cleaned = cleaned.substring(cleaned.length() - 80);
        }

        if (!cleaned.contains(".")) {
            cleaned = cleaned + ".jpg";
        }

        return cleaned;
    }

    public record PresignedPut(String s3Key, String url, long expiresInSeconds) {
    }
}