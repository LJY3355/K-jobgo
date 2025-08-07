/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.spring.config.SecurityConfig
 *  com.spring.controller.MainController
 *  com.spring.entity.Admin
 *  com.spring.masked.service.ProfileUserService
 *  com.spring.page.dto.ProfilePage
 *  com.spring.profile.masked.dto.MaskedProfileDto
 *  jakarta.servlet.http.HttpSession
 *  org.springframework.stereotype.Controller
 *  org.springframework.ui.Model
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.RequestParam
 */
package com.spring.controller;

import com.spring.config.SecurityConfig;
import com.spring.entity.Admin;
import com.spring.masked.service.ProfileUserService;
import com.spring.page.dto.ProfilePage;
import com.spring.profile.masked.dto.MaskedProfileDto;
import jakarta.servlet.http.HttpSession;
import java.util.List;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {
    private final SecurityConfig securityConfig;
    private final ProfileUserService profileUserService;

    public MainController(SecurityConfig securityConfig, ProfileUserService profileUserService) {
        this.securityConfig = securityConfig;
        this.profileUserService = profileUserService;
    }

    @GetMapping(value={"/"})
    public String home() {
        return "redirect:/home";
    }

    @GetMapping("/loginPage")
    public String loginPage(Model model, CsrfToken csrfToken) {
        model.addAttribute("_csrf", csrfToken);
        return "login";
    }

    @GetMapping(value={"/home"})
    public String main(HttpSession session, Model model) {
        Admin admin = (Admin)session.getAttribute("loggedInAdmin");
        model.addAttribute("isAdmin", (Object)(admin != null ? 1 : 0));
        if (admin != null) {
            model.addAttribute("adminName", (Object)admin.getAdminName());
        }
        return "main";
    }

    @GetMapping(value={"/companyInfo"})
    public String companyInfo(HttpSession session, Model model) {
        Admin admin = (Admin)session.getAttribute("loggedInAdmin");
        model.addAttribute("isAdmin", (Object)(admin != null ? 1 : 0));
        if (admin != null) {
            model.addAttribute("adminName", (Object)admin.getAdminName());
            model.addAttribute("authorityId", (Object)admin.getAuthorityType().getAuthorityId());
        } else {
            model.addAttribute("authorityId", (Object)0);
        }
        return "company";
    }

    @GetMapping(value={"/profileList"})
    public String maskedProfileList(@RequestParam(defaultValue="1") int page, @RequestParam(defaultValue="10") int size, @RequestParam(required=false) String desiredLocation, @RequestParam(required=false) String nationality, @RequestParam(required=false) String gender, @RequestParam(required=false) String visaType, @RequestParam(required=false) String keyword, Model model) {
        String nationalityType = null;
        List<String> excludeNationalities = null;
        if ("\uae30\ud0c0".equals(nationality)) {
            nationalityType = "etc";
            excludeNationalities = List.of("\ub124\ud314", "\ubabd\uace8", "\ubbf8\uc580\ub9c8", "\ubc29\uae00\ub77c\ub370\uc2dc", "\ubca0\ud2b8\ub0a8", "\uc2a4\ub9ac\ub791\uce74", "\uc6b0\uc988\ubca0\ud0a4\uc2a4\ud0c4", "\uc778\ub3c4", "\uc778\ub3c4\ub124\uc2dc\uc544", "\uce84\ubcf4\ub514\uc544", "\ud0a4\ub974\ud0a4\uc2a4\uc2a4\ud0c4", "\ud30c\ud0a4\uc2a4\ud0c4", "\ud544\ub9ac\ud540");
            nationality = null;
        } else if (nationality != null && !nationality.isEmpty()) {
            nationalityType = null;
        } else {
            nationality = null;
            nationalityType = null;
        }
        ProfilePage maskedPage = this.profileUserService.getMaskedProfilePage(page, size, desiredLocation, nationality, gender, visaType, keyword, nationalityType, excludeNationalities);
        model.addAttribute("profileList", (Object)maskedPage.getMaskedProfiles());
        model.addAttribute("pageInfo", (Object)maskedPage);
        model.addAttribute("isAdmin", (Object)false);
        model.addAttribute("desiredLocation", (Object)desiredLocation);
        model.addAttribute("nationality", (Object)nationality);
        model.addAttribute("gender", (Object)gender);
        model.addAttribute("visaType", (Object)visaType);
        model.addAttribute("keyword", (Object)keyword);
        return "userProfileList";
    }

    @GetMapping(value={"/profileDetail/{profileId}"})
    public String maskedProfileDetail(@PathVariable Long profileId, Model model) {
        MaskedProfileDto maskedProfile = this.profileUserService.getMaskedProfileDetail(profileId);
        if (maskedProfile == null) {
            return "redirect:/profileList";
        }
        model.addAttribute("profileDto", (Object)maskedProfile);
        model.addAttribute("isAdmin", (Object)false);
        return "userProfile";
    }

    @GetMapping(value={"/matchingPage"})
    public String partnerCompany() {
        return "MatchingPage";
    }
    
    @GetMapping("/MatchingPage")
    public String showMatchingPage() {
        return "MatchingPage";  // MatchingPage.html 뷰 반환
    }
}

