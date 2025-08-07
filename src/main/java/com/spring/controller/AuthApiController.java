package com.spring.controller;

import com.spring.dto.LoginRequestDto;
import com.spring.dto.LoginResponseDto;
import com.spring.entity.Admin;
import com.spring.service.AdminService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AuthApiController {

    private final AdminService adminService;

    public AuthApiController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/api/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginDTO, HttpSession session) {
        System.out.println("✅ 로그인 컨트롤러 진입함");
        System.out.println("✅ 전달받은 admin_login_id: [" + loginDTO.getAdminLoginId() + "]");
        System.out.println("✅ 전달받은 password: [" + loginDTO.getAdminPassword() + "]");

        Admin admin = adminService.authenticate(loginDTO.getAdminLoginId(), loginDTO.getAdminPassword());

        if (admin != null) {
        	 // 권한 제한 (예: 4번 권한은 접근 불가) — 먼저 체크
            System.out.println("authorityId: " + admin.getAuthorityType().getAuthorityId());
            // 권한 제한 (예: 4번 권한은 접근 불가)
            if (admin.getAuthorityType().getAuthorityId() == 4) {
                System.out.println("퇴사자라서 거부: authorityId == 4");
                return ResponseEntity.status(403).body(null);
            }
            
            // 세션에 관리자 정보 저장
            session.setAttribute("loggedInAdmin", admin);

            // Spring Security에 인증 정보 설정
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(admin, null, List.of());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

            System.out.println("Authentication 세팅 후 SecurityContext: " + SecurityContextHolder.getContext().getAuthentication());

            // 로그인 응답 생성
            LoginResponseDto responseDto = new LoginResponseDto(
                    admin.getAdminId(),
                    admin.getAdminName(),
                    admin.getAuthorityType().getAuthorityName(),
                    session.getId()
            );
            System.out.println("LoginResponseDto 생성 후 반환: " + responseDto);
            return ResponseEntity.ok(responseDto);
        }

        // 인증 실패
        System.out.println("로그인 실패: 아이디/비밀번호 불일치 또는 존재하지 않음");
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/api/keep-alive")
    @ResponseBody
    public String keepSessionAlive(HttpSession session) {
        session.getAttribute("loggedInAdmin"); // 세션 유지 확인
        return "alive";
    }

    @GetMapping("/api/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate(); // 세션 무효화
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/admin-test")
    public ResponseEntity<List<Admin>> testAdminSelect() {
        List<Admin> allAdmins = adminService.findAll();
        return ResponseEntity.ok(allAdmins);
    }
}
