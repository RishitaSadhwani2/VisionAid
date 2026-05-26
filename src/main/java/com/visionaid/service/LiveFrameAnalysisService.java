package com.visionaid.service;

import org.springframework.stereotype.Service;

@Service
public class LiveFrameAnalysisService {

    private final AnalysisService analysisService;

    public LiveFrameAnalysisService(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    public String analyzeLiveFrame(String base64Image) {

        if (base64Image == null || base64Image.isBlank()) {
            return "No camera frame received.";
        }

        try {
            return analysisService.analyzeLiveBase64Image(base64Image);
        } catch (Exception e) {
            e.printStackTrace();
            return "I am unable to analyze the live scene right now.";
        }
    }
}