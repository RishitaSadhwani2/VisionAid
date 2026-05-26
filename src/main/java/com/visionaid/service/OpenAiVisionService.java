package com.visionaid.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;
import java.util.Map;

@Service
public class OpenAiVisionService {

    private final RestClient restClient;
    private final String model;

    public OpenAiVisionService(
            @Value("${openai.apiKey}") String apiKey,
            @Value("${openai.model}") String model
    ) {
        this.model = model;
        this.restClient = RestClient.builder()
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    public String createVisionSummary(
            String imageUrl,
            List<String> rekognitionLabels,
            String mode,
            boolean hazardDetected
    ) {
        String prompt = """
You are VisionAid, a real-time accessibility assistant for visually impaired users.

Analyze the image from the user's point of view and give a short spoken response.

Your goal:
Help the user understand what is around them and warn them only if there is a real safety concern.

Rules:
- Speak in 1 to 2 short sentences.
- Be direct, natural, and useful for audio.
- If there is danger, start with "Warning."
- Mention the danger clearly, such as stairs, vehicle, fire, smoke, sharp object, crowded area, obstacle, water, or blocked path.
- Give a simple action if needed, like "move carefully", "slow down", or "avoid moving forward."
- If there is no danger, calmly describe the scene.
- Do not say "Rekognition detected."
- Do not over-warn unless the image clearly suggests risk.
- If the scene includes a recognizable religious or cultural figure, you may identify it carefully.

Mode: %s
Backend hazard detected from labels: %s
Rekognition labels: %s
""".formatted(mode, hazardDetected, rekognitionLabels);

        Map<String, Object> body = Map.of(
                "model", model,
                "input", List.of(
                        Map.of(
                                "role", "user",
                                "content", List.of(
                                        Map.of("type", "input_text", "text", prompt),
                                        Map.of("type", "input_image", "image_url", imageUrl)
                                )
                        )
                )
        );

        try {
            Map response = restClient.post()
                    .uri("/responses")
                    .body(body)
                    .retrieve()
                    .body(Map.class);

            return extractText(response);

        } catch (RestClientResponseException e) {
            throw new RuntimeException("OpenAI API error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        } catch (Exception e) {
            throw new RuntimeException("OpenAI summary generation failed: " + e.getMessage(), e);
        }
    }

    private String extractText(Map response) {
        try {
            List output = (List) response.get("output");
            Map firstOutput = (Map) output.get(0);
            List content = (List) firstOutput.get("content");
            Map firstContent = (Map) content.get(0);
            return firstContent.get("text").toString();
        } catch (Exception e) {
            throw new RuntimeException("Could not parse OpenAI response: " + response);
        }
    }
}