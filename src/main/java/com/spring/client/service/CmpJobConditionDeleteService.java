package com.spring.client.service;

import com.spring.client.dto.CmpJobConditionDeleteDto;
import java.util.List;

public interface CmpJobConditionDeleteService {
    CmpJobConditionDeleteDto logDeletion(CmpJobConditionDeleteDto dto);
    List<CmpJobConditionDeleteDto> findHistoryByJobId(Long jobId);
}
