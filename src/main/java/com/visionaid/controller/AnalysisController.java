package com.visionaid.controller;

import com.visionaid.dto.AnalysisRequest;
import com.visionaid.dto.AnalysisResponse;
import com.visionaid.service.AnalysisService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/analysis")
public class AnalysisController {

    private final AnalysisService analysisService;

    public AnalysisController(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @PostMapping
    public AnalysisResponse analyze(@Valid @RequestBody AnalysisRequest request) {
        return analysisService.analyze(
                request.getS3Key(),
                request.getMode()
        );
    }
}