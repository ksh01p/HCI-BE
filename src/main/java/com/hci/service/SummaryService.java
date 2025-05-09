package com.hci.service;

import com.hci.dto.SummaryResponseDto;
import com.hci.dto.TextRequestDto;

public interface SummaryService {
    SummaryResponseDto summarize(TextRequestDto textRequestDto);
}
