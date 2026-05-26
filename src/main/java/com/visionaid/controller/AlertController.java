package com.visionaid.controller;

import com.visionaid.service.SnsAlertService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/alert")
public class AlertController {

    private final SnsAlertService snsAlertService;

    public AlertController(SnsAlertService snsAlertService) {
        this.snsAlertService = snsAlertService;
    }

    @PostMapping
    public String sendAlert(@RequestBody AlertRequest request) {

        boolean sent = snsAlertService.sendEmergencyAlert(
                request.getDescription(),
                request.getS3Key()
        );

        if (sent) {
            return "Emergency alert sent successfully.";
        }

        return "Failed to send emergency alert.";
    }

    public static class AlertRequest {

        private String description;
        private String s3Key;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getS3Key() {
            return s3Key;
        }

        public void setS3Key(String s3Key) {
            this.s3Key = s3Key;
        }
    }
}