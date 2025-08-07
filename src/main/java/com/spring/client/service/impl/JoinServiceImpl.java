package com.spring.client.service.impl;

import com.spring.client.dto.request.JoinRequestDTO;
import com.spring.client.entity.*;
import com.spring.client.enums.FileCategory;
import com.spring.client.enums.JobStatus;
import com.spring.client.repository.*;
import com.spring.client.service.EmailService;
import com.spring.client.service.JoinService;
import com.spring.service.AdminRejectionService;
import com.spring.service.FileService;

import jakarta.mail.MessagingException;

import java.io.IOException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class JoinServiceImpl implements JoinService {

    private final CmpInfoRepository         infoRepo;
    private final CmpContRepository         contRepo;
    private final CmpAttachRepository       attachRepo;
    private final CmpJobConditionRepository jobRepo;
    private final PasswordEncoder           passwordEncoder;
    private final FileService            fileService;      //  S3FileService 주입
    private final EmailService 		emailService;	//  EmailService 주입
    
    @Override
    public void register(JoinRequestDTO joinRequestDto) {
        // 1) cmp_info 저장
        CmpInfo cmpInfo = CmpInfo.builder()
            .cmpName(joinRequestDto.getCmpName())
            .ceoName(joinRequestDto.getCeoName())
            .bizNo(joinRequestDto.getBizNo())
            .bizEmail(joinRequestDto.getBizEmail())
            .bizPwd(passwordEncoder.encode(joinRequestDto.getBizPwd()))
            .zipCode(joinRequestDto.getZipCode())
            .cmpAddr(joinRequestDto.getCmpAddr())
            .addrDt(joinRequestDto.getAddrDt())
            .cmpPhone(joinRequestDto.getCmpPhone())
            .prxJoin(joinRequestDto.getPrxJoin())    
            .proxyExecutor(joinRequestDto.getProxyExecutor())
            .fileConfirm(joinRequestDto.isFileConfirm())
            .agrTerms(joinRequestDto.isAgrTerms())
            .build();
        cmpInfo = infoRepo.save(cmpInfo);

        // 2) cmp_cont 저장
        CmpCont cont = CmpCont.builder()
            .cmpInfo(cmpInfo)
            .empName(joinRequestDto.getEmpName())
            .empTitle(joinRequestDto.getEmpTitle())
            .empPhone(joinRequestDto.getEmpPhone())
            .build();
        contRepo.save(cont);

        // 3) cmp_attach 저장 (사업자등록증, 명함)
        saveAttachment(cmpInfo, joinRequestDto.getBizFileLicense(), FileCategory.BUSINESS_LICENSE);
        saveAttachment(cmpInfo, joinRequestDto.getBizFileCard(),    FileCategory.BUSINESS_CARD);

        // 4) 구인조건 저장 (선택 입력)
        if (joinRequestDto.getJobType() != null && !joinRequestDto.getJobType().isBlank()) {
            CmpJobCondition job = CmpJobCondition.builder()
                .cmpInfo(cmpInfo)
                .jobType(joinRequestDto.getJobType())
                .desiredNationality(joinRequestDto.getDesiredNationality())
                .desiredCount(joinRequestDto.getDesiredCount())
                .jobCategory(joinRequestDto.getJobCategory())
                .experience(joinRequestDto.getExperience())
                .education(joinRequestDto.getEducation())
                .qualification(joinRequestDto.getQualification())
                .workingHours(joinRequestDto.getWorkingHours())
                .breakTime(joinRequestDto.getBreakTime())
                .employmentType(joinRequestDto.getEmploymentType())
                .insurance(joinRequestDto.getInsurance())
                .retirementPay(joinRequestDto.getRetirementPay())
                .totalCount(joinRequestDto.getTotalCount())
                .currentForeigners(joinRequestDto.getCurrentForeigners())
                .dormitory(joinRequestDto.getDormitory())
                .meal(joinRequestDto.getMeal())
                .jobDescription(joinRequestDto.getJobDescription())
                .major(joinRequestDto.getMajor())
                .computerSkills(joinRequestDto.getComputerSkills())
                .languageSkills(joinRequestDto.getLanguageSkills())
                .preferredConditions(joinRequestDto.getPreferredConditions())
                .otherPreferredConditions(joinRequestDto.getOtherPreferredConditions())
                .otherNotes(joinRequestDto.getOtherNotes())
                .status(JobStatus.ACTIVE)
                .build();
            jobRepo.save(job);
        }
        
        // 5) 가입신청 확인 이메일 발송
        String subject = "[K-jobgo] 회원가입 신청 접수 안내";
        try {
            emailService.sendRegistrationNotification(
                cmpInfo.getBizEmail(),   // 수신자
                subject,                 // 제목
                joinRequestDto           // 템플릿 바인딩용 DTO
            );
        } catch (MessagingException e) {
            // 메일 발송 실패 시 처리 (로그, 예외 재던지기 등)
            log.error("가입접수 확인 메일 발송 실패", e);
        }
    }
    
    private void saveAttachment(CmpInfo info, MultipartFile file, FileCategory cat) {
        try {
            String url = fileService.upload(file);
            CmpAttach attach = CmpAttach.builder()
                .cmpInfo(info)
                .fCat(cat)
                .origName(file.getOriginalFilename())
                .fPath(url)
                .fExt(getExtension(file))
                .fMime(file.getContentType())
                .fSize(file.getSize())
                .build();
            attachRepo.save(attach);
        } catch (IOException e) {
            // 업로드 실패 시 로깅하거나, RuntimeException 으로 wrapping
            throw new RuntimeException("파일 업로드 중 오류가 발생했습니다.", e);
        }
    }

    private String getExtension(MultipartFile file) {
        String name = file.getOriginalFilename();
        return name != null && name.contains(".")
            ? name.substring(name.lastIndexOf('.') + 1)
            : "";
    }
}