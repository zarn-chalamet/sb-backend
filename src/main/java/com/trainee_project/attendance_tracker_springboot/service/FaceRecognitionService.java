package com.trainee_project.attendance_tracker_springboot.service;

import com.trainee_project.attendance_tracker_springboot.dto.FaceVerificationResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class FaceRecognitionService {

    private final WebClient webClient;

    public FaceRecognitionService(WebClient.Builder webClientBuilder,
                                  @Value("${face.recognition.url}") String faceRecognitionUrl) {
        this.webClient = webClientBuilder.baseUrl(faceRecognitionUrl).build();
    }


    public FaceVerificationResponseDto verifyFace(byte[] referenceImage, byte[] liveImage) {
        ByteArrayResource referenceResource = new ByteArrayResource(referenceImage) {
            @Override
            public String getFilename() {
                return "reference.jpg";
            }
        };
        ByteArrayResource liveResource = new ByteArrayResource(liveImage) {
            @Override
            public String getFilename() {
                return "live.jpg";
            }
        };

        return webClient.post()
                .uri("/api/face/verify")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData("reference_file", referenceResource)
                        .with("live_file", liveResource))
                .retrieve()
                .bodyToMono(FaceVerificationResponseDto.class)
                .block(); // blocking for synchronous call
    }
}
