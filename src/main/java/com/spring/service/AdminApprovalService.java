package com.spring.service;

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
public class AdminApprovalService {
    private final CmpInfoRepository cmpInfoRepository;
    private final EmailService emailService;

    public void approveCompany(Long cmpInfoId) {
        CmpInfo cmpInfo = cmpInfoRepository.findById(cmpInfoId)
            .orElseThrow(() -> new EntityNotFoundException("회사정보 없음"));
        cmpInfo.approve();
        cmpInfoRepository.save(cmpInfo);

        // 메일 발송 시 예외를 잡아서 처리
        try {
            emailService.sendApprovalNotification(
                cmpInfo.getBizEmail(),   // to
                cmpInfo.getCmpName(),    // companyName
                cmpInfo.getBizEmail()    // loginId
            );
        } catch (MessagingException e) {
            log.error("승인 완료 안내 메일 발송 실패: cmpInfoId={}, to={}", 
                      cmpInfoId, cmpInfo.getBizEmail(), e);
            // 필요시 알림용 커스텀 런타임 예외 던지기
            // throw new MailSendRuntimeException("메일 발송 실패", e);
        }
    }
}
