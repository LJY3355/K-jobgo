package com.spring.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.client.entity.CmpInfo;
import com.spring.client.repository.CmpInfoRepository;
import com.spring.client.service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j  
@Service
@RequiredArgsConstructor
@Transactional
public class AdminRejectionService {
    private final CmpInfoRepository cmpInfoRepository;
    private final EmailService emailService;

    public void rejectCompany(Long cmpInfoId, List<String> reasons) {
        CmpInfo cmpInfo = cmpInfoRepository.findById(cmpInfoId)
            .orElseThrow(() -> new EntityNotFoundException("회사정보 없음"));

        cmpInfo.reject();
        cmpInfoRepository.save(cmpInfo);

        try {
            // 3) 반려 메일 발송
            emailService.sendRejectionNotification(
                cmpInfo.getBizEmail(),
                cmpInfo.getCmpName(),
                reasons
            );
        } catch (MessagingException e) {
            log.error("반려 안내 HTML 메일 발송 실패: cmpInfoId={}, to={}",
                      cmpInfoId, cmpInfo.getBizEmail(), e);
            // 필요시 사용자 알림용 런타임 예외를 던지거나,
            // 별도 모니터링 시스템에 통보하는 로직 추가
        }
    }
}

