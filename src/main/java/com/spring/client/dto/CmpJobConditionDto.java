package com.spring.client.dto;

import com.spring.client.enums.JobStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CmpJobConditionDto {
    private Long jobId;
    private Long cmpId;

    private String jobType;
    private String desiredNationality;
    private Integer desiredCount;
    private String jobCategory;
    private String experience;
    private String education;
    private String qualification;
    private String workingHours;
    private String breakTime;
    private String employmentType;
    private String insurance;
    private String retirementPay;
    private Integer totalCount;
    private Integer currentForeigners;
    private String dormitory;
    private String meal;
    private String jobDescription;
    private String major;
    private String computerSkills;
    private String languageSkills;
    private String preferredConditions;
    private String otherPreferredConditions;
    private String otherNotes;

    private JobStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime completedAt;
    private LocalDateTime cancelledAt;
}
