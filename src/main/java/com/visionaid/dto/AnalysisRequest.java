package com.visionaid.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class AnalysisRequest {

    @NotBlank(message = "s3Key is required")
    private String s3Key;

    @NotBlank(message = "mode is required")
    @Pattern(
            regexp = "^(SCENE|TEXT|HAZARD)$",
            message = "mode must be SCENE, TEXT, or HAZARD"
    )
    private String mode;

    public String getS3Key() {
        return s3Key;
    }

    public String getMode() {
        return mode;
    }

    public void setS3Key(String s3Key) {
        this.s3Key = s3Key;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}