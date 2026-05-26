package com.visionaid.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.DetectLabelsRequest;
import software.amazon.awssdk.services.rekognition.model.DetectLabelsResponse;
import software.amazon.awssdk.services.rekognition.model.Image;
import software.amazon.awssdk.services.rekognition.model.S3Object;

import java.util.List;

@Service
public class RekognitionService {

    private final RekognitionClient rekognitionClient;
    private final String uploadsBucket;

    public RekognitionService(
            RekognitionClient rekognitionClient,
            @Value("${visionaid.aws.uploadsBucket}") String uploadsBucket
    ) {
        this.rekognitionClient = rekognitionClient;
        this.uploadsBucket = uploadsBucket;
    }

    public List<String> detectLabels(String s3Key) {
        S3Object s3Object = S3Object.builder()
                .bucket(uploadsBucket)
                .name(s3Key)
                .build();

        Image image = Image.builder()
                .s3Object(s3Object)
                .build();

        DetectLabelsRequest request = DetectLabelsRequest.builder()
                .image(image)
                .maxLabels(10)
                .minConfidence(75F)
                .build();

        DetectLabelsResponse response = rekognitionClient.detectLabels(request);

        return response.labels()
                .stream()
                .map(label -> label.name() + " (" + String.format("%.1f", label.confidence()) + "%)")
                .toList();
    }
}