/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.spring.controller.TestController
 *  com.spring.entity.Admin
 *  com.spring.entity.AuthorityType
 *  com.spring.repository.AdminRepository
 *  com.spring.repository.AuthorityTypeRepository
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.spring.controller;

import com.spring.client.service.EmailService;
import com.spring.entity.Admin;
import com.spring.entity.AuthorityType;
import com.spring.repository.AdminRepository;
import com.spring.repository.AuthorityTypeRepository;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {
	
	  private final EmailService emailService;
	  
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private AuthorityTypeRepository authorityTypeRepository;

    @GetMapping(value={"/api/hello"})
    public String hello() {
        return "Hello from Spring Boot!";
    }

    @GetMapping(value={"/List"})
    public List<Admin> getAllAdmins() {
        return this.adminRepository.findAll();
    }

    @GetMapping(value={"/AuthorityList"})
    public List<AuthorityType> getAllAuthorityType() {
        return this.authorityTypeRepository.findAll();
    }
    
    @GetMapping("/mail/test")
    public ResponseEntity<String> test() throws MessagingException {
        emailService.sendRejectionNotification(
            "dlwndudgg@naver.com",
            "이주영테스트회사",
            List.of("이건 테스트 메일입니다")
        );
        return ResponseEntity.ok("메일 발송 시도 완료");
    }
}

