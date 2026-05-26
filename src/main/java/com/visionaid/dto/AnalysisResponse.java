package com.visionaid.dto;

import java.util.List;

public class AnalysisResponse {

    private String s3Key;
    private String mode;
    private String summary;
    private List<String> labels;
    private boolean hazardDetected;
    private boolean emergencyAlertSent;
    private String audioS3Key;
    private String audioUrl;

    public AnalysisResponse(
            String s3Key,
            String mode,
            String summary,
            List<String> labels,
            boolean hazardDetected,
            boolean emergencyAlertSent,
            String audioS3Key,
            String audioUrl
    ) {
        this.s3Key = s3Key;
        this.mode = mode;
        this.summary = summary;
        this.labels = labels;
        this.hazardDetected = hazardDetected;
        this.emergencyAlertSent = emergencyAlertSent;
        this.audioS3Key = audioS3Key;
        this.audioUrl = audioUrl;
    }

    public String getS3Key() {
        return s3Key;
    }

    public String getMode() {
        return mode;
    }

    public String getSummary() {
        return summary;
    }

    public List<String> getLabels() {
        return labels;
    }

    public boolean isHazardDetected() {
        return hazardDetected;
    }

    public boolean isEmergencyAlertSent() {
        return emergencyAlertSent;
    }

    public String getAudioS3Key() {
        return audioS3Key;
    }

    public String getAudioUrl() {
        return audioUrl;
    }
}