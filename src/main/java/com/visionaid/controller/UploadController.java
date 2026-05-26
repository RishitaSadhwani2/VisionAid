package com.visionaid.controller;

import com.visionaid.dto.PresignRequest;
import com.visionaid.dto.PresignResponse;
import com.visionaid.service.PresignService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/uploads")
public class UploadController {

    private final PresignService presignService;

    public UploadController(PresignService presignService) {
        this.presignService = presignService;
    }

    @PostMapping("/presign")
    public PresignResponse presign(@Valid @RequestBody PresignRequest request) {
        var result = presignService.createPresignedPut(
                request.getFileName(),
                request.getContentType()
        );

        return new PresignResponse(
                result.s3Key(),
                result.url(),
                result.expiresInSeconds()
        );
    }
}