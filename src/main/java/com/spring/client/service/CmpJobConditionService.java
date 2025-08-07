package com.spring.client.service;

import com.spring.client.dto.CmpJobConditionDto;
import com.spring.client.enums.JobStatus;

import java.util.List;

public interface CmpJobConditionService {
    CmpJobConditionDto create(CmpJobConditionDto dto);
    CmpJobConditionDto update(Long jobId, CmpJobConditionDto dto);
    void cancel(Long jobId, String reason, String cancelledBy);
    void complete(Long jobId, String completedBy);
    CmpJobConditionDto findById(Long jobId);
    List<CmpJobConditionDto> findByCmpIdAndStatus(Long cmpId, JobStatus status);
}
