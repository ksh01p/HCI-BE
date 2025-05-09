package com.hci.service;

import org.springframework.web.multipart.MultipartFile;

public interface DagloApiService {
    /**
     * Daglo API에 음성 파일을 보내어 텍스트로 변환한 결과를 반환합니다.
     */
    String transcribe(MultipartFile file) throws Exception;
}
