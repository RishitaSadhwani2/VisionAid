package com.visionaid.service;

import com.visionaid.dto.AnalysisResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.List;

@Service
public class AnalysisService {

    private final RekognitionService rekognitionService;
    private final PollyService pollyService;
    private final SnsAlertService snsAlertService;
    private final OpenAiVisionService openAiVisionService;
    private final S3Presigner s3Presigner;
    private final S3Client s3Client;

    private final String uploadsBucket;
    private final String resultsBucket;

    public AnalysisService(
            RekognitionService rekognitionService,
            PollyService pollyService,
            SnsAlertService snsAlertService,
            OpenAiVisionService openAiVisionService,
            S3Presigner s3Presigner,
            S3Client s3Client,
            @Value("${visionaid.aws.uploadsBucket}") String uploadsBucket,
            @Value("${visionaid.aws.resultsBucket}") String resultsBucket
    ) {
        this.rekognitionService = rekognitionService;
        this.pollyService = pollyService;
        this.snsAlertService = snsAlertService;
        this.openAiVisionService = openAiVisionService;
        this.s3Presigner = s3Presigner;
        this.s3Client = s3Client;
        this.uploadsBucket = uploadsBucket;
        this.resultsBucket = resultsBucket;
    }

    public AnalysisResponse analyze(String s3Key, String mode) {

        List<String> labels = rekognitionService.detectLabels(s3Key);

        boolean hazardDetected = detectHazard(labels);

        String imageUrl = createPresignedGetUrl(uploadsBucket, s3Key);

        String summary = openAiVisionService.createVisionSummary(
                imageUrl,
                labels,
                mode,
                hazardDetected
        );

        String audioS3Key = pollyService.createSpeechAndUploadToS3(summary);

        String audioUrl = createPresignedGetUrl(resultsBucket, audioS3Key);

        boolean emergencyAlertSent = false;

        if ("HAZARD".equalsIgnoreCase(mode) && hazardDetected) {
            emergencyAlertSent =
                    snsAlertService.sendEmergencyAlert(summary, s3Key);
        }

        return new AnalysisResponse(
                s3Key,
                mode,
                summary,
                labels,
                hazardDetected,
                emergencyAlertSent,
                audioS3Key,
                audioUrl
        );
    }

    public String analyzeLiveBase64Image(String base64Image) {

        try {

            String cleanBase64 = base64Image.replaceFirst(
                    "^data:image/[^;]+;base64,",
                    ""
            );

            byte[] imageBytes =
                    Base64.getDecoder().decode(cleanBase64);

            String s3Key =
                    "live-frames/frame-"
                            + Instant.now().toEpochMilli()
                            + ".jpg";

            PutObjectRequest putObjectRequest =
                    PutObjectRequest.builder()
                            .bucket(uploadsBucket)
                            .key(s3Key)
                            .contentType("image/jpeg")
                            .build();

            s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromBytes(imageBytes)
            );

            List<String> labels =
                    rekognitionService.detectLabels(s3Key);

            boolean hazardDetected =
                    detectHazard(labels);

            String imageUrl =
                    createPresignedGetUrl(
                            uploadsBucket,
                            s3Key
                    );

            return openAiVisionService.createVisionSummary(
                    imageUrl,
                    labels,
                    "SCENE",
                    hazardDetected
            );

        } catch (Exception e) {
            e.printStackTrace();
            return "I am unable to analyze the live scene right now.";
        }
    }

    private String createPresignedGetUrl(
            String bucket,
            String key
    ) {

        GetObjectRequest getObjectRequest =
                GetObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .build();

        GetObjectPresignRequest presignRequest =
                GetObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofMinutes(10))
                        .getObjectRequest(getObjectRequest)
                        .build();

        return s3Presigner
                .presignGetObject(presignRequest)
                .url()
                .toString();
    }

    private boolean detectHazard(List<String> labels) {

        String joinedLabels =
                String.join(" ", labels).toLowerCase();

        return joinedLabels.contains("fire")
                || joinedLabels.contains("knife")
                || joinedLabels.contains("weapon")
                || joinedLabels.contains("car")
                || joinedLabels.contains("vehicle")
                || joinedLabels.contains("traffic")
                || joinedLabels.contains("road")
                || joinedLabels.contains("stairs")
                || joinedLabels.contains("smoke");
    }
}