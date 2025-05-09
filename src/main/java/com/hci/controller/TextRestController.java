package com.hci.controller;

import com.hci.domain.TextRequest;
import com.hci.dto.TextRequestDto;
import com.hci.repository.TextRequestRepository;
import com.hci.service.DagloApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/text")
public class TextRestController {

    private final DagloApiService dagloApiService;
    private final TextRequestRepository textRequestRepository;

    @Autowired
    public TextRestController(
            DagloApiService dagloApiService,
            TextRequestRepository textRequestRepository
    ) {
        this.dagloApiService = dagloApiService;
        this.textRequestRepository = textRequestRepository;
    }

    @PostMapping("/upload-audio")
    public ResponseEntity<TextRequestDto> uploadAudio(@RequestParam("file") MultipartFile file)
            throws Exception {
        // 1) Daglo API로 음성 → 텍스트 변환
        String transcript = dagloApiService.transcribe(file);

        // 2) DB에 저장
        TextRequest saved = textRequestRepository
                .save(new TextRequest(null, transcript));

        // 3) DTO로 변환하여 반환
        TextRequestDto dto = new TextRequestDto(saved.getId(), saved.getContent());
        return ResponseEntity.ok(dto);
    }
}
