// js/common.js
window.addEventListener("DOMContentLoaded", function () {
  console.log("공통 JS 실행");

  const toggleBtn = document.querySelector(".menu-toggle");
  const menu = document.getElementById("menu");

  // 1. 토글 버튼 클릭 시 메뉴 열고/닫기
  if (toggleBtn && menu) {
    toggleBtn.addEventListener("click", function () {
      menu.classList.toggle("hidden-menu");
    });
  }

  // 2. 외부 클릭 시 메뉴 닫기
  document.addEventListener("click", function (e) {
    if (menu && toggleBtn && 
        !menu.classList.contains("hidden-menu") && // 열려있을 때만 처리
        !menu.contains(e.target) &&
        !toggleBtn.contains(e.target)) {
      menu.classList.add("hidden-menu");
    }
  });
  
    // ★ data-is-admin 값 읽어서 boolean 변환 (true / 1 둘 다 true 처리)
    const isAdminAttr = document.body.getAttribute('data-is-admin');
    const isAdmin = (isAdminAttr === 'true' || isAdminAttr === '1');

    // 로그인 버튼 동작 정의
    const loginBtn = document.getElementById("loginBtn");
    if (loginBtn) {
      loginBtn.addEventListener("click", function (e) {
        e.preventDefault();

        if (isAdmin) {
          // 로그아웃 API 호출
          fetch("/api/logout", { method: "GET" })
            .then(() => {
              alert("로그아웃 되었습니다.");
              window.location.href = "/home";
            })
            .catch(() => alert("로그아웃 중 오류가 발생했습니다."));
        } else {
          // login.js의 openLoginModal() 호출
          if (typeof openLoginModal === "function") {
            openLoginModal();
          } else {
            document.getElementById("loginModal").style.display = "flex";
          }
        }
      });
    }
  });
