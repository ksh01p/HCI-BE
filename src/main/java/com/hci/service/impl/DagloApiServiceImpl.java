package com.hci.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.hci.service.DagloApiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.client.MultipartBodyBuilder;

@Service
public class DagloApiServiceImpl implements DagloApiService {

    private final WebClient webClient;

    public DagloApiServiceImpl(@Value("${daglo.api.key}") String dagloApiKey) {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.daglo.ai/recognition")
                .defaultHeader("Authorization", "Bearer " + dagloApiKey)
                .build();
    }

    @Override
    public String transcribe(MultipartFile file) throws Exception {
        // MultipartBodyBuilder로 파일 파트 생성
        byte[] bytes = file.getBytes();
        ByteArrayResource resource = new ByteArrayResource(bytes) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", resource)
                .header("Content-Disposition",
                        "form-data; name=file; filename=" + file.getOriginalFilename());

        MultiValueMap<String, HttpEntity<?>> multipartData = builder.build();

        // Daglo API 호출
        JsonNode response = webClient.post()
                .uri("/speech-to-text")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(multipartData))
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        // JSON에서 텍스트 필드 추출
        return response.get("text").asText();
    }
}
