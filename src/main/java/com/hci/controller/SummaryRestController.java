package com.hci.controller;

import com.hci.domain.TextRequest;
import com.hci.dto.SummaryResponseDto;
import com.hci.dto.TextRequestDto;
import com.hci.repository.TextRequestRepository;
import com.hci.service.SummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SummaryRestController {

    private final SummaryService summaryService;
    private final TextRequestRepository textRequestRepository;

    @Autowired
    public SummaryRestController(
            SummaryService summaryService,
            TextRequestRepository textRequestRepository
    ) {
        this.summaryService = summaryService;
        this.textRequestRepository = textRequestRepository;
    }

    @PostMapping("/summarize/{id}")
    public ResponseEntity<SummaryResponseDto> summarize(@PathVariable("id") Integer id) {
        // 저장된 텍스트 조회
        TextRequest request = textRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 ID: " + id));

        // DTO로 매핑
        TextRequestDto dto = new TextRequestDto(request.getId(), request.getContent());

        // 요약 수행
        SummaryResponseDto summary = summaryService.summarize(dto);
        return ResponseEntity.ok(summary);
    }
}
