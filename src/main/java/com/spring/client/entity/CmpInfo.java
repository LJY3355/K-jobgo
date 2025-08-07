package com.spring.client.entity;

import com.spring.client.enums.ApprStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor  // 모든 필드를 인자로 받는 생성자 자동 생성
@Builder            // builder() 메서드 자동 생성
@ToString(exclude = {"contacts","attachments","approvalHistory"})
@Entity
@Table(name = "cmp_info")
public class CmpInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cmp_id")
    private Long cmpId;

    @Column(name = "cmp_name", nullable = false, length = 255)
    private String cmpName;

    @Column(name = "ceo_name", nullable = false, length = 100)
    private String ceoName;

    @Column(name = "biz_no", nullable = false, unique = true, length = 20)
    private String bizNo;

    @Column(name = "biz_email", nullable = false, unique = true, length = 255)
    private String bizEmail;

    @Column(name = "biz_pwd", nullable = false, length = 255)
    private String bizPwd;

    @Column(name = "zip_code", nullable = false, length = 10)
    private String zipCode;

    @Column(name = "cmp_addr", nullable = false, length = 255)
    private String cmpAddr;

    @Column(name = "addr_dt", nullable = false, length = 255)
    private String addrDt;

    @Column(name = "cmp_phone", length = 50)
    private String cmpPhone;

    @Column(name = "agr_terms", nullable = false)
    private boolean agrTerms;

    @Column(name = "prx_join", nullable = false)
    private boolean prxJoin;

    @AssertTrue(message = "첨부파일 확인 동의 여부를 선택해 주세요.")
    @Column(name = "file_confirm", nullable = false)
    private boolean fileConfirm;

    @Column(name = "proxy_executor", length = 100)
    private String proxyExecutor;

    @Column(name = "is_del", nullable = false)
    private boolean isDel;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "del_dt")
    private LocalDateTime delDt;

    @Builder.Default  
    @Enumerated(EnumType.STRING)
    @Column(name = "appr_status", nullable = false, length = 10)
    private ApprStatus apprStatus = ApprStatus.PENDING;

    @Column(name = "crt_dt", nullable = false, updatable = false)
    private LocalDateTime crtDt;

    @Column(name = "upd_dt", nullable = false)
    private LocalDateTime updDt;

    @OneToMany(mappedBy = "cmpInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CmpCont> contacts;

    @OneToMany(mappedBy = "cmpInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CmpAttach> attachments;

    @OneToMany(mappedBy = "cmpInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CmpApprHist> approvalHistory;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.crtDt = now;
        this.updDt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updDt = LocalDateTime.now();
    }
    
    // 가입 승인 
    public void approve() {
        this.apprStatus = ApprStatus.APPROVED;
      }

    // 가입 반려
      public void reject() {
        this.apprStatus = ApprStatus.REJECTED;
      }

}
