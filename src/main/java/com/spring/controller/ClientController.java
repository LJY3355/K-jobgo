package com.spring.controller;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.spring.client.dto.request.JoinRequestDTO;
import com.spring.client.service.JoinService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ClientController {
    private final JoinService joinService;

    // 회원가입 페이지 보여주기
    @GetMapping("/client/joinPage")
    public String showClientJoinPage(Model model, CsrfToken csrfToken) {
        model.addAttribute("_csrf", csrfToken);
        model.addAttribute("joinReq", new JoinRequestDTO());
        return "client/join";  // join.html 뷰 반환
    }
    
    // 회원가입 form 제출 처리 
    @PostMapping("/join")
    public String submitJoin(
            @ModelAttribute("joinReq") @Valid JoinRequestDTO joinRequestDto,
            BindingResult binding) {
        if (binding.hasErrors()) {
            return "client/join"; // 검증 오류 시 다시 폼으로
        }

        // 서비스 호출해서 가입 로직 수행(저장 및 메일 발송)
        joinService.register(joinRequestDto);

        return "redirect:/client/join-success";
    }
    
    // 회원가입 신청(확인) 페이지
    @GetMapping("/client/join-success")
    public String joinSuccess() {
        return "client/join-success";
    }
}
