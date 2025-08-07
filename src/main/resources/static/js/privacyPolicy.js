document.addEventListener('DOMContentLoaded', function() {
  const openBtn = document.getElementById('openPrivacyPolicy');
  const modal = document.getElementById('privacyModal');
  const closeBtn = document.getElementById('closePrivacyModal');

  if (!openBtn) {
    console.error('openPrivacyPolicy element not found');
    return;
  }
  if (!modal) {
    console.error('privacyModal element not found');
    return;
  }
  if (!closeBtn) {
    console.error('closePrivacyModal element not found');
    return;
  }

  openBtn.addEventListener('click', function(e) {
    e.preventDefault();
    modal.classList.add('show'); // 모달 열기
  });

  closeBtn.addEventListener('click', function() {
    modal.classList.remove('show'); // 모달 닫기
  });

  modal.addEventListener('click', function(e) {
    if (e.target === modal) {
      modal.classList.remove('show'); // 모달 닫기 (배경 클릭 시)
    }
  });
});
