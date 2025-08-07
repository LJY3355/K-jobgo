package com.spring.client.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.spring.client.entity.CmpJobCondition;
import com.spring.client.enums.JobStatus;

import java.util.List;

@Repository
public interface CmpJobConditionRepository extends JpaRepository<CmpJobCondition, Long> {
	List<CmpJobCondition> findByCmpInfoCmpIdAndStatus(Long cmpId, JobStatus status);
}
