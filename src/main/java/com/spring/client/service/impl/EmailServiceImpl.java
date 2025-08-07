package com.spring.client.service.impl;

import com.spring.client.dto.request.JoinRequestDTO;
import com.spring.client.service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Slf4j  
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;  // Thymeleaf 템플릿 엔진
    @Value("${spring.mail.username}") 
    private String from;

    @Override
    public void sendJoinConfirmation(String to, String subject, JoinRequestDTO joinRequestDto) {
        // 더 이상 사용하지 않음. 템플릿 버전으로 대체할 예정.
    }

    public void sendRegistrationNotification(String to,
            String subject,
            JoinRequestDTO joinRequestDto) throws MessagingException {
	MimeMessage message = mailSender.createMimeMessage();
	MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
	helper.setFrom(from);
	helper.setTo(to);
	helper.setSubject(subject);
	
	// Thymeleaf 컨텍스트에 변수 바인딩
	Context ctx = new Context();
	ctx.setVariable("companyName", joinRequestDto.getCmpName());
	
	// 템플릿 렌더링
	String html = templateEngine.process("join-confirmation", ctx);
	helper.setText(html, true);
	
	mailSender.send(message);
	log.info("가입접수 확인 메일 전송: to={}", to);
	}
    
    @Override
    public void sendApprovalNotification(String to, String companyName, String loginId)
    		throws MessagingException {
        Context ctx = new Context();
        ctx.setVariable("companyName", companyName);
        ctx.setVariable("loginId", loginId);
        String htmlBody = templateEngine.process("email/approval", ctx);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject("[K-jobgo] 회원가입 승인 완료 안내");
        helper.setText(htmlBody, true);
        mailSender.send(message);
        
        log.info("승인 완료 안내 HTML 메일 전송: to={}, loginId={}", to, loginId);
      }

	@Override
	public void sendRejectionNotification(String to, String companyName, List<String> reasons)                   
			throws MessagingException {
	    // 1) 템플릿 변수 세팅
	    Context ctx = new Context();
	    ctx.setVariable("companyName", companyName);
	    ctx.setVariable("reasons", reasons);

	    // 2) HTML 본문 렌더링 (src/main/resources/templates/email/rejection.html)
	    String htmlBody = templateEngine.process("email/rejection", ctx);

	    // 3) MimeMessage 생성 및 설정
	    MimeMessage message = mailSender.createMimeMessage();
	    MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
	    helper.setFrom(from);
	    helper.setTo(to);
	    helper.setSubject("[K-jobgo] 회원가입 신청 반려 안내");
	    helper.setText(htmlBody, true);  // true → HTML 모드

	    // 4) 메일 전송
	    mailSender.send(message);
	    log.info("반려 안내 HTML 메일 전송: to={}", to);
	}
}
