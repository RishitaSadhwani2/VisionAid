package com.visionaid.controller;



import com.visionaid.dto.AnalyzeFrameRequest;
import com.visionaid.dto.AnalyzeFrameResponse;
import com.visionaid.service.LiveFrameAnalysisService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")

public class LiveAnalysisController {

    private final LiveFrameAnalysisService liveFrameAnalysisService;

    public LiveAnalysisController(LiveFrameAnalysisService liveFrameAnalysisService) {
        this.liveFrameAnalysisService = liveFrameAnalysisService;
    }

    @PostMapping("/analyze-frame")
    public AnalyzeFrameResponse analyzeFrame(@RequestBody AnalyzeFrameRequest request) {

        String summary = liveFrameAnalysisService.analyzeLiveFrame(request.getImage());

        return new AnalyzeFrameResponse(summary);
    }
}
