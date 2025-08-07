package com.spring.client.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cmp_job_condition_del")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CmpJobConditionDelete {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "del_id")
    private Long delId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_id", nullable = false)
    private CmpJobCondition jobCondition;

    @Column(name = "del_reason", nullable = false, length = 255)
    private String delReason;

    @Column(name = "del_by", nullable = false, length = 100)
    private String delBy;

    @Column(name = "del_at", nullable = false, updatable = false)
    private LocalDateTime delAt;

    @Lob
    @Column(name = "original_data", columnDefinition = "LONGTEXT", nullable = false)
    private String originalData;

    @PrePersist
    private void onCreate() {
        this.delAt = LocalDateTime.now();
    }
}
