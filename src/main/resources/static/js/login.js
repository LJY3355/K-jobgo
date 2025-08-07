document.addEventListener('DOMContentLoaded', function () {
  const closeBtn = document.querySelector('#loginModal .close');
  const submitLoginBtn = document.getElementById('submitLoginBtn');

  const tabButtons = document.querySelectorAll('.tab-btn');
  const tabContents = document.querySelectorAll('.tab-content');
  const modal = document.getElementById('loginModal');

  if (modal) {
    modal.style.display = 'none';
  }

  function openLoginModal() {
    if (modal) modal.style.display = 'flex';
  }

  function closeLoginModal() {
    if (modal) modal.style.display = 'none';
  }

  function switchTab(targetTabId) {
    tabButtons.forEach(btn => {
      btn.classList.toggle('active', btn.getAttribute('data-tab') === targetTabId);
    });
    tabContents.forEach(content => {
      content.classList.toggle('active', content.id === targetTabId);
    });
  }

  if (closeBtn) {
    closeBtn.addEventListener('click', closeLoginModal);
  }

  window.addEventListener('click', (e) => {
    if (e.target === modal) {
      closeLoginModal();
    }
  });

  tabButtons.forEach(button => {
    button.addEventListener('click', () => {
      const tabId = button.getAttribute('data-tab');
      switchTab(tabId);
    });
  });

  if (submitLoginBtn) {
    submitLoginBtn.addEventListener('click', function () {
      const loginId = document.getElementById('userId').value;
      const password = document.getElementById('password').value;

      // CSRF 값 가져오기 (템플릿에서 meta로 넣어줘야 함)
      const csrfTokenMeta = document.querySelector('meta[name="_csrf"]');
      const csrfHeaderMeta = document.querySelector('meta[name="_csrf_header"]');
      const headers = {
        'Content-Type': 'application/json'
      };
      if (csrfTokenMeta && csrfHeaderMeta) {
        const csrfToken = csrfTokenMeta.getAttribute('content');
        const csrfHeader = csrfHeaderMeta.getAttribute('content');
        if (csrfToken && csrfHeader) {
          headers[csrfHeader] = csrfToken;
        }
      }

      fetch('/api/login', {
        method: 'POST',
        credentials: 'include',
        headers: headers,
        body: JSON.stringify({
          adminLoginId: loginId,
          adminPassword: password
        })
      })
        .then(response => {
          if (response.status === 403) {
            // 실제로는 권한 문제일 수도 있고 CSRF 실패일 수도 있으니 구분 짓지 말고 일반 에러 처리
            throw new Error('권한 없음 또는 인증 실패');
          } else if (response.ok) {
            return response.json();
          } else {
            throw new Error('일반실패');
          }
        })
        .then(data => {
          alert(`${data.adminName}님 로그인 성공`);

          setInterval(() => {
            fetch('/api/keep-alive', { method: 'GET', credentials: 'include' });
          }, 300000);

          closeLoginModal();
          window.location.href = "/home";
        })
        .catch(err => {
          if (err.message === '권한 없음 또는 인증 실패') {
            alert('퇴사자이거나 접근 권한이 없습니다.');
          } else {
            alert('아이디 또는 비밀번호가 일치하지 않습니다.');
          }
          document.getElementById('password').value = '';
        });
    });
  }
});
