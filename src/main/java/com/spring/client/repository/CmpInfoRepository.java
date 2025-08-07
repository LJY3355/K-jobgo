package com.spring.client.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.spring.client.entity.CmpInfo;

@Repository
public interface CmpInfoRepository extends JpaRepository<CmpInfo, Long> {
	  boolean existsByBizEmail(String bizEmail);
}
