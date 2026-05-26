package com.visionaid.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

@Service
public class SnsAlertService {

    private final SnsClient snsClient;
    private final String topicArn;

    public SnsAlertService(
            SnsClient snsClient,
            @Value("${visionaid.aws.sns.topic-arn}") String topicArn
    ) {
        this.snsClient = snsClient;
        this.topicArn = topicArn;
    }

    public boolean sendEmergencyAlert(String summary, String s3Key) {
        String message = """
                VisionAid Emergency Alert

                A possible hazard was detected.

                Summary:
                %s

                Image S3 Key:
                %s
                """.formatted(summary, s3Key);

        PublishRequest request = PublishRequest.builder()
                .topicArn(topicArn)
                .subject("VisionAid Emergency Alert")
                .message(message)
                .build();

        snsClient.publish(request);

        return true;
    }
}