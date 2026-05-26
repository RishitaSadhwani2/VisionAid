package com.visionaid.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class PresignRequest {

    @NotBlank(message = "fileName is required")
    private String fileName;

    @NotBlank(message = "contentType is required")
    @Pattern(
            regexp = "^image/(jpeg|jpg|png)$",
            message = "contentType must be image/jpeg, image/jpg, or image/png"
    )
    private String contentType;

    @NotBlank(message = "mode is required")
    @Pattern(
            regexp = "^(SCENE|TEXT|HAZARD)$",
            message = "mode must be SCENE, TEXT, or HAZARD"
    )
    private String mode;

    public String getFileName() {
        return fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public String getMode() {
        return mode;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}