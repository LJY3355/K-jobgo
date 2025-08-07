package com.spring.client.entity;

import com.spring.client.enums.JobStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "cmp_job_condition")
public class CmpJobCondition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id")
    private Long jobId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cmp_id", nullable = false)
    private CmpInfo cmpInfo;

    @Column(name = "job_type", length = 255)
    private String jobType;

    @Column(name = "desired_nationality", length = 100)
    private String desiredNationality;

    @Column(name = "desired_count")
    private Integer desiredCount;

    @Column(name = "job_category", length = 255)
    private String jobCategory;

    @Column(length = 100)
    private String experience;

    @Column(length = 100)
    private String education;

    @Column(length = 255)
    private String qualification;

    @Column(name = "working_hours", columnDefinition = "TEXT")
    private String workingHours;

    @Column(name = "break_time", length = 100)
    private String breakTime;

    @Column(name = "employment_type", length = 100)
    private String employmentType;

    @Column(length = 255)
    private String insurance;

    @Column(name = "retirement_pay", length = 255)
    private String retirementPay;

    @Column(name = "total_count")
    private Integer totalCount;

    @Column(name = "current_foreigners")
    private Integer currentForeigners;

    @Column(length = 255)
    private String dormitory;

    @Column(length = 255)
    private String meal;

    @Column(name = "job_description", columnDefinition = "TEXT")
    private String jobDescription;

    @Column(length = 255)
    private String major;

    @Column(name = "computer_skills", length = 255)
    private String computerSkills;

    @Column(name = "language_skills", length = 255)
    private String languageSkills;

    @Column(name = "preferred_conditions", length = 255)
    private String preferredConditions;

    @Column(name = "other_preferred_conditions", length = 255)
    private String otherPreferredConditions;

    @Column(name = "other_notes", columnDefinition = "TEXT")
    private String otherNotes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private JobStatus status = JobStatus.ACTIVE;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
