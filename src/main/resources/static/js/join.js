// join.js

$(function() {
  // ---------- CSRF 토큰 세팅 ----------
  const token  = $('meta[name="_csrf"]').attr('content');
  const header = $('meta[name="_csrf_header"]').attr('content');
  if (token && header) {
    $.ajaxSetup({
      beforeSend: xhr => xhr.setRequestHeader(header, token)
    });
  }

  // ---------- 1) 사업자등록번호 조회 ----------
  window.verifyBusinessNumber = function() {
    const p1 = $('#companyNumber1').val().trim();
    const p2 = $('#companyNumber2').val().trim();
    const p3 = $('#companyNumber3').val().trim();
    if (!(p1 && p2 && p3)) {
      alert('⚠️ 사업자등록번호를 모두 입력하세요.');
      $('#companyNumber1').focus();
      return;
    }
    $('#bizNo').val(`${p1}-${p2}-${p3}`);
    $.ajax({
      url: "/api/business/verify",
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify({ b_no: [p1+p2+p3] }),
      dataType: "json",
      success(result) {
        const $r = $('#bizCheckResult')
          .removeClass('success error')
          .text('');
        if (result.status_code === "OK" && result.data?.[0]) {
          if (result.data[0].b_stt_cd === "01") {
            $r.addClass('success').text("✅ 사업자등록번호 인증 성공!");
          } else {
            $r.addClass('error')
              .text(`❌ 유효하지 않습니다. 사유: ${result.data[0].b_stt || '없음'}`);
          }
        } else {
          $r.addClass('error').text("❌ API 응답이 비정상입니다.");
        }
      },
      error() {
        $('#bizCheckResult')
          .removeClass('success')
          .addClass('error')
          .text("❌ 인증 중 오류가 발생했습니다.");
      }
    });
  };

  // ---------- 2) 이메일 입력 & 중복 확인 ----------
  window.handleEmailDomainChange = function() {
    const sel = $('#emailDomainSelect').val();
    if (sel === 'custom') {
      $('#email2').show().val('').focus();
    } else {
      $('#email2').hide().val(sel);
    }
    updateBizEmail();
  };
  window.checkEmailDuplicate = function() {
    const a = $('#email1').val().trim();
    const b = $('#email2').val().trim();
    if (!a || !b) {
      alert('⚠️ 이메일을 모두 입력해주세요.');
      $('#email1').focus();
      return;
    }
    const email = `${a}@${b}`;
    $('#bizEmail').val(email);
    $.ajax({
      url: '/api/check-email',
      method: 'POST',
      contentType: 'application/json',
      data: JSON.stringify({ email }),
      success(res) {
        $('#emailCheckResult')
          .text(res.duplicate
            ? '❌ 이미 사용 중인 이메일입니다.'
            : '✅ 사용 가능한 이메일입니다.')
          .css('color', res.duplicate ? 'red' : 'green');
      },
      error() {
        $('#emailCheckResult')
          .text('❌ 이메일 확인 중 오류 발생')
          .css('color', 'red');
      }
    });
  };
  $('#email1,#email2').on('input change', updateBizEmail);
  function updateBizEmail() {
    const a = $('#email1').val().trim();
    const b = $('#email2').val().trim();
    if (a && b) $('#bizEmail').val(`${a}@${b}`);
  }

  // ---------- 3) 주소 검색 ----------
  window.openKakaoPostcode = function() {
    new daum.Postcode({
      oncomplete(data) {
        $('#postcode').val(data.zonecode);
        $('#companyAddress').val(data.address);
        $('#detailAddress').val('').focus();
      }
    }).open();
  };

  // ---------- 4) 대리 가입 동의 토글 ----------
  $('input[name="prxJoin"]').on('change', function() {
    const isProxy = $('#fileConfirmProxy').is(':checked');
    const $grp    = $('#proxyExecutorGroup');
    if (isProxy) {
      $grp.show();
      $('#proxyExecutor').prop('required', true).focus();
    } else {
      $grp.hide();
      $('#proxyExecutor').prop('required', false).val('');
    }
  });

  // ---------- 5) 실시간 비밀번호 검사 (10자 이상) ----------
  const pwIn  = $('#password');
  const cpwIn = $('#confirmPassword');
  const pwHint  = $('<small class="check-result"></small>').insertAfter(pwIn);
  const cpwHint = $('<small class="check-result"></small>').insertAfter(cpwIn);
  function isValidPassword(v) {
    return /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{10,}$/.test(v);
  }
  function validatePassword() {
    const pw  = pwIn.val();
    const cpw = cpwIn.val();
    if (pw && !isValidPassword(pw)) {
      pwHint.text('❌ 영문자+숫자 포함 최소 10자입니다.').css('color','red');
    } else if (pw) {
      pwHint.text('✅ 형식이 유효합니다.').css('color','green');
    } else {
      pwHint.text('');
    }
    if (cpw) {
      if (pw !== cpw) {
        cpwHint.text('❌ 일치하지 않습니다.').css('color','red');
      } else {
        cpwHint.text('✅ 일치합니다.').css('color','green');
      }
    } else {
      cpwHint.text('');
    }
  }
  pwIn.on('input', validatePassword);
  cpwIn.on('input', validatePassword);

  // ---------- 6) 기타 항목 토글 ----------
  $('#dormitoryOtherChk').on('change', () => {
    $('#dormitoryOtherTxt').toggle(!!$('#dormitoryOtherChk').is(':checked')).val('');
  });
  $('#mealOtherChk').on('change', () => {
    $('#mealOtherTxt').toggle(!!$('#mealOtherChk').is(':checked')).val('');
  });

  // ---------- 7) 파일 첨부 힌트 ----------
  ['bizFileLicense','bizFileCard'].forEach(id => {
    $(`#${id}`).on('change', function() {
      const file = this.files[0];
      const hint = $(`#${id}Hint`);
      if (!file) {
        hint.text('jpg, png 또는 PDF 파일을 첨부해주세요.').css('color','red');
      } else if (!['image/jpeg','image/png','application/pdf'].includes(file.type)) {
        hint.text('❌ 허용되지 않는 파일 형식입니다.').css('color','red');
        this.value = '';
      } else {
        hint.text(`✅ "${file.name}"이(가) 첨부되었습니다.`).css('color','green');
      }
    });
  });

  // 폼 제출 바인딩
  $('#joinForm').on('submit', handleJoinSubmit);
});

// ---------- 복합 히든 필드 세팅 ----------
function fillHiddenCompositeFields() {
  // 사업자번호
  const p1 = $('#companyNumber1').val().trim();
  const p2 = $('#companyNumber2').val().trim();
  const p3 = $('#companyNumber3').val().trim();
  if (p1||p2||p3) $('#bizNo').val(`${p1}-${p2}-${p3}`);
  // 이메일
  const a = $('#email1').val().trim();
  const b = $('#email2').val().trim();
  if (a && b) $('#bizEmail').val(`${a}@${b}`);
  // 회사 연락처
  const cp1 = $('input[name="cmpPhone1"]').val().trim();
  const cp2 = $('input[name="cmpPhone2"]').val().trim();
  const cp3 = $('input[name="cmpPhone3"]').val().trim();
  if (cp1||cp2||cp3) $('#cmpPhone').val(`${cp1}-${cp2}-${cp3}`);
  // 담당자 연락처
  const ep1 = $('input[name="empPhone1"]').val().trim();
  const ep2 = $('input[name="empPhone2"]').val().trim();
  const ep3 = $('input[name="empPhone3"]').val().trim();
  if (ep1||ep2||ep3) $('#empPhone').val(`${ep1}-${ep2}-${ep3}`);
}

// ---------- 폼 제출 처리 (순서대로) ----------
function handleJoinSubmit(e) {
  e.preventDefault();

  // 1) 회사·대표자 입력
  if (!$('#cmpName').val().trim()) {
    alert('⚠️ 회사명을 입력해 주세요.'); $('#cmpName').focus(); return;
  }
  if (!$('#ceoName').val().trim()) {
    alert('⚠️ 대표자 성명을 입력해 주세요.'); $('#ceoName').focus(); return;
  }

  // 2) 사업자등록번호 인증
  if (!$('#bizCheckResult').hasClass('success')) {
    alert('⚠️ 사업자등록번호 인증을 먼저 진행해 주세요.');
    $('#companyNumber1').focus();
    return;
  }

  // 3) 이메일 중복 확인
  if (!$('#bizEmail').val().trim()) {
    alert('⚠️ 이메일을 입력해 주세요.'); $('#email1').focus(); return;
  }
  if (!$('#emailCheckResult').text().includes('✅')) {
    alert('⚠️ 이메일 중복 확인을 진행해 주세요.');
    $('#email1').focus();
    return;
  }

  // 4) 비밀번호 형식 및 일치 검사
  const pw    = $('#password').val();
  const cpw   = $('#confirmPassword').val();
  const pwPat = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{10,}$/;
  if (!pwPat.test(pw)) {
    alert('❌ 비밀번호는 영문자+숫자 포함 최소 10자이어야 합니다.');
    $('#password').focus(); return;
  }
  if (pw !== cpw) {
    alert('❌ 비밀번호가 일치하지 않습니다.');
    $('#confirmPassword').focus(); return;
  }

  // 5) 주소 검색 & 상세 주소
  if (!$('#postcode').val()) {
    alert('⚠️ 우편번호를 입력해 주세요.'); $('#postcode').focus(); return;
  }
  if (!$('#detailAddress').val().trim()) {
    alert('⚠️ 상세 주소를 입력해 주세요.'); $('#detailAddress').focus(); return;
  }

  // 6) 연락처
  // 회사 연락처 (cmpPhone1~3)
  const cp1 = $('input[name="cmpPhone1"]').val().trim();
  const cp2 = $('input[name="cmpPhone2"]').val().trim();
  const cp3 = $('input[name="cmpPhone3"]').val().trim();
  if (!(cp1 && cp2 && cp3)) {
    alert('⚠️ 회사 연락처를 모두 입력해 주세요.');
    if (!cp1) {
      $('input[name="cmpPhone1"]').focus();
    } else if (!cp2) {
      $('input[name="cmpPhone2"]').focus();
    } else {
      $('input[name="cmpPhone3"]').focus();
    }
    return;
  }

  // 담당자 연락처 (empPhone1~3)
  const ep1 = $('input[name="empPhone1"]').val().trim();
  const ep2 = $('input[name="empPhone2"]').val().trim();
  const ep3 = $('input[name="empPhone3"]').val().trim();
  if (!(ep1 && ep2 && ep3)) {
    alert('⚠️ 담당자 연락처를 모두 입력해 주세요.');
    if (!ep1) {
      $('input[name="empPhone1"]').focus();
    } else if (!ep2) {
      $('input[name="empPhone2"]').focus();
    } else {
      $('input[name="empPhone3"]').focus();
    }
    return;
  }


  // 7) 파일 첨부
  if (!$('#bizFileLicense')[0].files.length) {
    alert('⚠️ 사업자등록증 사본을 첨부해 주세요.');
    $('#bizFileLicense').focus(); 
	return;
  }
  if (!$('#bizFileCard')[0].files.length) {
    alert('⚠️ 담당자 명함을 첨부해 주세요.');
    $('#bizFileCard').focus(); 
	return;
  }

  // 8) 첨부파일 확인 동의 라디오 반드시 하나 선택
  const prxVal = $('input[name="prxJoin"]:checked').val();
  if (prxVal === undefined) {
    alert('⚠️ “대리 가입” 또는 “직접 가입” 중 하나를 선택해 주세요.');
    $('input[name="prxJoin"]').first().focus();
    return;
  }

  // 9) 대리 회원가입 동의 시 직원명
  if ($('#proxyExecutorGroup').is(':visible') &&
      !$('#proxyExecutor').val().trim()) {
    alert('⚠️ 대리회원가입 동의 시 처리 직원명을 입력해 주세요.');
    $('#proxyExecutor').focus(); 
	return;
  }

  // 10) 약관 동의
  if (!$('#agreeTerms').is(':checked')) {
    alert('⚠️ 이용약관에 동의하셔야 합니다.');
    $('#agreeTerms').focus(); 
	return;
  }

  // 최종: 히든 필드 세팅 + 모달 띄우기
  fillHiddenCompositeFields();
  $('#customConfirm').removeClass('hidden');
}

// ---------- 모달 확인/취소 ----------
function submitForm() {
  fillHiddenCompositeFields();
  $('#joinForm')[0].submit();
}
function closeConfirm() {
  $('#customConfirm').addClass('hidden');
}
