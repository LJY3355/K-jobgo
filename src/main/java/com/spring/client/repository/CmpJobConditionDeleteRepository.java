package com.spring.client.repository;

import com.spring.client.entity.CmpJobConditionDelete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CmpJobConditionDeleteRepository
        extends JpaRepository<CmpJobConditionDelete, Long> {
    List<CmpJobConditionDelete> findByJobConditionJobId(Long jobId);
}
