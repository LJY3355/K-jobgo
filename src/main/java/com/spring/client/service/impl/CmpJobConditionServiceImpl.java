package com.spring.client.service.impl;

import com.spring.client.dto.CmpJobConditionDto;
import com.spring.client.entity.CmpJobCondition;
import com.spring.client.enums.JobStatus;
import com.spring.client.repository.CmpJobConditionRepository;
import com.spring.client.service.CmpJobConditionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CmpJobConditionServiceImpl implements CmpJobConditionService {

    private final CmpJobConditionRepository repo;

    @Override
    @Transactional
    public CmpJobConditionDto create(CmpJobConditionDto dto) {
        // TODO: mapping DTO→Entity, save, 반환
        return null;
    }

    @Override
    @Transactional
    public CmpJobConditionDto update(Long jobId, CmpJobConditionDto dto) {
        // TODO: find, 검증, field 수정, save, 반환
        return null;
    }

    @Override
    @Transactional
    public void cancel(Long jobId, String reason, String cancelledBy) {
        // TODO: 상태 변경 + 로그 삽입
    }

    @Override
    @Transactional
    public void complete(Long jobId, String completedBy) {
        // TODO: 상태 변경
    }

    @Override
    @Transactional(readOnly = true)
    public CmpJobConditionDto findById(Long jobId) {
        // TODO: findById → DTO
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CmpJobConditionDto> findByCmpIdAndStatus(Long cmpId, JobStatus status) {
        return repo.findByCmpInfoCmpIdAndStatus(cmpId, status)
                   .stream()
                   .map(entity -> CmpJobConditionDto.builder()
                       .jobId(entity.getJobId())
                       .cmpId(entity.getCmpInfo().getCmpId())
                       .jobType(entity.getJobType())
                       .desiredNationality(entity.getDesiredNationality())
                       .desiredCount(entity.getDesiredCount())
                       .jobCategory(entity.getJobCategory())
                       .experience(entity.getExperience())
                       .education(entity.getEducation())
                       .qualification(entity.getQualification())
                       .workingHours(entity.getWorkingHours())
                       .breakTime(entity.getBreakTime())
                       .employmentType(entity.getEmploymentType())
                       .insurance(entity.getInsurance())
                       .retirementPay(entity.getRetirementPay())
                       .totalCount(entity.getTotalCount())
                       .currentForeigners(entity.getCurrentForeigners())
                       .dormitory(entity.getDormitory())
                       .meal(entity.getMeal())
                       .jobDescription(entity.getJobDescription())
                       .major(entity.getMajor())
                       .computerSkills(entity.getComputerSkills())
                       .languageSkills(entity.getLanguageSkills())
                       .preferredConditions(entity.getPreferredConditions())
                       .otherPreferredConditions(entity.getOtherPreferredConditions())
                       .otherNotes(entity.getOtherNotes())
                       .status(entity.getStatus())
                       .createdAt(entity.getCreatedAt())
                       .updatedAt(entity.getUpdatedAt())
                       .completedAt(entity.getCompletedAt())
                       .cancelledAt(entity.getCancelledAt())
                       .build()
                   )
                   .collect(Collectors.toList());
    }

}
