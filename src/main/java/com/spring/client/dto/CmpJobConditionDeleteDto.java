package com.spring.client.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CmpJobConditionDeleteDto {
    private Long delId;
    private Long jobId;
    private String delReason;
    private String delBy;
    private LocalDateTime delAt;
    private String originalData;
}
