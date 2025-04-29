// main.js
window.addEventListener("DOMContentLoaded", function () {
  console.log("✅ 메인 JS 실행됨");

  const searchInput = document.getElementById("searchInput");
  const dropdownList = document.getElementById("dropdownList");

  if (searchInput && dropdownList) {
    // 입력창 클릭 시 리스트 보이기
    searchInput.addEventListener("focus", () => {
      dropdownList.classList.remove("hidden");
    });

    // 리스트 클릭 시 선택한 값 입력창에 넣기
    dropdownList.addEventListener("click", function (e) {
      if (e.target.tagName === "LI") {
        searchInput.value = e.target.textContent;
        dropdownList.classList.add("hidden");
      }
    });
  }

  // 만약 검색 버튼 기능도 추가하고 싶다면 여기 작성 가능!
});

const sloganEl = document.querySelector('.main-slogan');
const fullText = "Take Root in Korea";
let index = 0;
let isErasing = false;

function animateSlogan() {
  if (!isErasing) {
    sloganEl.textContent = fullText.slice(0, index + 1);
    index++;

    if (index === fullText.length) {
      // 다 썼으면 일정 시간 유지 후 fade out
      setTimeout(() => {
        sloganEl.style.opacity = 0;

        setTimeout(() => {
          // 리셋
          index = 0;
          sloganEl.textContent = "";
          sloganEl.style.opacity = 1;
          animateSlogan();
        }, 400); // fade out 후 다시 시작
      }, 2000); // 전체 문장 유지 시간
      return;
    }
  }

  setTimeout(animateSlogan, 100); // 한 글자씩 등장 속도
}

animateSlogan();