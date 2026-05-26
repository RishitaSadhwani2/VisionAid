package com.visionaid.dto;



public class AnalyzeFrameResponse {
    private String summary;

    public AnalyzeFrameResponse(String summary) {
        this.summary = summary;
    }

    public String getSummary() {
        return summary;
    }
}