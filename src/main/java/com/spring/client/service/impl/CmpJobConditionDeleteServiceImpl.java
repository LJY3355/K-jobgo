package com.spring.client.service.impl;

import com.spring.client.dto.CmpJobConditionDeleteDto;
import com.spring.client.entity.CmpJobCondition;
import com.spring.client.entity.CmpJobConditionDelete;
import com.spring.client.repository.CmpJobConditionDeleteRepository;
import com.spring.client.repository.CmpJobConditionRepository;
import com.spring.client.service.CmpJobConditionDeleteService;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CmpJobConditionDeleteServiceImpl implements CmpJobConditionDeleteService {

    private final CmpJobConditionDeleteRepository deleteRepo;
    private final CmpJobConditionRepository jobCondRepo;  

    @Autowired
    public CmpJobConditionDeleteServiceImpl(
    	    CmpJobConditionDeleteRepository deleteRepo,
    	    CmpJobConditionRepository      jobCondRepo
    	) {
    	    this.deleteRepo = deleteRepo;  // ← 맞춰서 할당
    	    this.jobCondRepo = jobCondRepo;
    	}
    
    @Override
    @Transactional
    public CmpJobConditionDeleteDto logDeletion(CmpJobConditionDeleteDto dto) {
        // ② 삭제 대상 엔티티 조회
        CmpJobCondition condition = jobCondRepo.findById(dto.getJobId())
            .orElseThrow(() -> new EntityNotFoundException(
                "구인조건이 존재하지 않습니다. jobId=" + dto.getJobId()));

        // ③ builder에 jobCondition을 넘겨준다
        CmpJobConditionDelete entity = CmpJobConditionDelete.builder()
            .jobCondition(condition)           // ← 여기에 인스턴스 주입
            .delReason(dto.getDelReason())
            .delBy(dto.getDelBy())
            .originalData(dto.getOriginalData())
            .build();

        entity = deleteRepo.save(entity);

        return CmpJobConditionDeleteDto.builder()
            .delId(entity.getDelId())
            .jobId(condition.getJobId())
            .delReason(entity.getDelReason())
            .delBy(entity.getDelBy())
            .delAt(entity.getDelAt())
            .originalData(entity.getOriginalData())
            .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CmpJobConditionDeleteDto> findHistoryByJobId(Long jobId) {
        return deleteRepo.findByJobConditionJobId(jobId).stream()
            .map(e -> CmpJobConditionDeleteDto.builder()
                .delId(e.getDelId())
                .jobId(e.getJobCondition().getJobId())
                .delReason(e.getDelReason())
                .delBy(e.getDelBy())
                .delAt(e.getDelAt())
                .originalData(e.getOriginalData())
                .build())
            .collect(Collectors.toList());
    }
}
