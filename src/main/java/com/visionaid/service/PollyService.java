package com.visionaid.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.polly.PollyClient;
import software.amazon.awssdk.services.polly.model.OutputFormat;
import software.amazon.awssdk.services.polly.model.SynthesizeSpeechRequest;
import software.amazon.awssdk.services.polly.model.SynthesizeSpeechResponse;
import software.amazon.awssdk.services.polly.model.VoiceId;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.time.Instant;

@Service
public class PollyService {

    private final PollyClient pollyClient;
    private final S3Client s3Client;
    private final String resultsBucket;

    public PollyService(
            PollyClient pollyClient,
            S3Client s3Client,
            @Value("${visionaid.aws.resultsBucket}") String resultsBucket
    ) {
        this.pollyClient = pollyClient;
        this.s3Client = s3Client;
        this.resultsBucket = resultsBucket;
    }

    public String createSpeechAndUploadToS3(String text) {
        SynthesizeSpeechRequest request = SynthesizeSpeechRequest.builder()
                .text(text)
                .voiceId(VoiceId.JOANNA)
                .outputFormat(OutputFormat.MP3)
                .build();

        try (ResponseInputStream<SynthesizeSpeechResponse> audioStream =
                     pollyClient.synthesizeSpeech(request)) {

            byte[] audioBytes = audioStream.readAllBytes();

            String audioKey = "audio/audio-" +
                    Instant.now().toString().replace(":", "").replace(".", "") +
                    ".mp3";

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(resultsBucket)
                    .key(audioKey)
                    .contentType("audio/mpeg")
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(audioBytes));

            return audioKey;

        } catch (IOException e) {
            throw new RuntimeException("Failed to create Polly audio", e);
        }
    }
}