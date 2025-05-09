package com.hci.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.hci.domain.SummaryResponse;
import com.hci.dto.SummaryResponseDto;
import com.hci.dto.TextRequestDto;
import com.hci.repository.SummaryResponseRepository;
import com.hci.service.SummaryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SummaryServiceImpl implements SummaryService {

    private final WebClient webClient;
    private final SummaryResponseRepository summaryResponseRepository;

    public SummaryServiceImpl(
            @Value("${openai.api.key}") String openaiApiKey,
            SummaryResponseRepository summaryResponseRepository
    ) {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1/chat/completions")
                .defaultHeader("Authorization", "Bearer " + openaiApiKey)
                .build();
        this.summaryResponseRepository = summaryResponseRepository;
    }

    @Override
    public SummaryResponseDto summarize(TextRequestDto dto) {
        // 1) OpenAI API 호출 요청 본문 구성
        Map<String, Object> body = new HashMap<>();
        body.put("model", "gpt-3.5-turbo");
        body.put("messages", List.of(
                Map.of("role", "system", "content", "다음 내용을 간단히 요약해주세요."),
                Map.of("role", "user", "content", dto.getText())
        ));

        // 2) OpenAI 호출 및 결과 획득
        JsonNode response = webClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        String summaryText = response
                .get("choices")
                .get(0)
                .get("message")
                .get("content")
                .asText();

        // 3) 결과 저장
        SummaryResponse saved = summaryResponseRepository
                .save(new SummaryResponse(null, summaryText));

        // 4) DTO 반환
        return new SummaryResponseDto(saved.getId(), saved.getSummary());
    }
}
