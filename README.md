# VisionAid

VisionAid is an AI-powered voice-enabled assistant designed to support visually impaired users through real-time scene understanding, intelligent narration, and emergency awareness.

The system combines cloud-native backend engineering with AI-based image reasoning to help users better understand their surroundings through contextual voice guidance.

## Features

- Real-time scene analysis using AWS Rekognition
- Human-like contextual narration powered by AI reasoning
- Voice feedback using AWS Polly
- Emergency alert workflow using AWS SNS
- Secure image upload pipeline with pre-signed S3 URLs
- Spring Boot microservices architecture
- RESTful APIs with API-key security
- Cloud-based media storage using Amazon S3
- Live frontend integration using React + Vite
- Monitoring and health tracking using Spring Boot Actuator

## Tech Stack

### Backend
- Java 17
- Spring Boot
- REST APIs
- Maven

### Frontend
- React
- Vite
- Axios

### Cloud & AI
- AWS S3
- AWS Rekognition
- AWS Polly
- AWS SNS
- AWS IAM
- CloudWatch

## Architecture Flow

1. User uploads an image through the frontend
2. Backend generates a secure pre-signed S3 upload URL
3. Image is uploaded directly to Amazon S3
4. AI analysis service processes the scene
5. Context-aware narration is generated
6. AWS Polly converts narration into speech
7. Emergency alerts can be triggered through SNS if hazards are detected

## Future Enhancements

- Real-time live video stream analysis
- WebSocket-based continuous narration
- OCR-based text reading assistance
- Smart hazard prediction
- Navigation assistance
- LLM-powered contextual reasoning
- Event-driven distributed processing with Kafka

## Motivation

VisionAid was built with the goal of combining accessibility with modern cloud and AI technologies to create a meaningful real-world impact for visually impaired individuals.

## Author

Rishita Sadhwani
MS in Information Technology
Arizona State University
