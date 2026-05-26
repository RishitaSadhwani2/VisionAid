package com.visionaid.dto;

public class PresignResponse {

    private String s3Key;
    private String uploadUrl;
    private long expiresInSeconds;

    public PresignResponse(String s3Key, String uploadUrl, long expiresInSeconds) {
        this.s3Key = s3Key;
        this.uploadUrl = uploadUrl;
        this.expiresInSeconds = expiresInSeconds;
    }

    public String getS3Key() {
        return s3Key;
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public long getExpiresInSeconds() {
        return expiresInSeconds;
    }
}