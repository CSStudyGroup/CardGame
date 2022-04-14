window.onload = function(){
    // 카드가 0개 일 경우 예외 처리
    if (dto.length == 0) {
        alert("선택 된 카테고리의 카드가 없습니다.\n메인으로 돌아갑니다.");
        window.location = "/card";
    }

    // 모바일에 따른 변화
    const prevShape = document.querySelector(".prev");
    const nextShape = document.querySelector(".next");
    const card = document.querySelector(".container");
    function isMobile(){
        return /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent);
    }

    if (isMobile()) {
        prevShape.style.display = "none";
        nextShape.style.display = "none";
        card.style.marginLeft = "70px";
        card.style.marginRight = "70px";
    }
    else {
        prevShape.style.display = "flex";
        nextShape.style.display = "flex";
        card.style.marginLeft = "170px";
        card.style.marginRight = "170px";
    }

    // 문항 수 표시
    const counter = document.getElementById("counter");
    counter.innerText = "(문항 수 : " + dto.length + ")";

    // 카테고리 해싱
    let categoryMap = new Map();
    for (let i = 0; i < categoryDtoList.length; i++) {
        categoryMap.set(categoryDtoList[i].cid, categoryDtoList[i].cname);
    }

    // 카드 클릭 애니메이션
    const cardTitle = document.getElementById("card-title");
    const text = document.getElementById("card-text");
    const question = document.getElementById("card-question");
    const answer = document.getElementById("card-answer");
    const category = document.getElementById("card-category");
    var check = null;
    card.addEventListener("click", function () {
        if (check == null){
            card.classList.add("rotate");
            setTimeout(function () {
                if (text.innerText == question.innerText) {
                    cardTitle.innerText = "A.";
                    text.innerText = answer.innerText;
                }
                else {
                    cardTitle.innerText = "Q.";
                    text.innerText = question.innerText;
                }
            }, 500);
            check = setTimeout(function () {
                card.classList.remove("rotate")
                check = null;
            }, 1000);
        }
    })

    // 문항 수 관련 변수
    const min = 0
    var max = dto.length - 1;
    var now = 0

    // 슬라이드 관련
    let start_x, end_x;

    document.addEventListener('touchstart', touch_start);
    document.addEventListener('touchend', touch_end);

    function touch_start(event) {
        start_x = event.touches[0].pageX
    }

    function touch_end(event) {
        end_x = event.changedTouches[0].pageX;
        if (start_x > end_x + 100){
            nextMotion();
        }
        else if (start_x < end_x - 100){
            prevMotion();
        }
    }

    // 이전 문제 진행 요청
    const prev = document.getElementById("prev");
    prev.addEventListener("click", prevMotion);
    function prevMotion() {
        if (check == null){
            now = (now - 1) - (dto.length * Math.floor((now - 1)/dto.length));

            card.classList.add("prevshow");
            setTimeout(function () {
                cardTitle.innerText = "Q.";
                category.innerText = "(" + categoryMap.get(dto[now].cid) + ")";
                text.innerText = dto[now].question;
                question.innerText = dto[now].question;
                answer.innerText = dto[now].answer;
            }, 500);
            check = setTimeout(function () {
                card.classList.remove("prevshow");
                check = null;
            }, 1000);
        }
    }

    // 다음 문제 진행 요청
    const next = document.getElementById("next");
    next.addEventListener("click", nextMotion);
    function nextMotion() {
        if (check == null){
            now = (now + 1) % dto.length;

            card.classList.add("nextshow");
            setTimeout(function () {
                cardTitle.innerText = "Q.";
                category.innerText = "(" + categoryMap.get(dto[now].cid) + ")";
                text.innerText = dto[now].question;
                question.innerText = dto[now].question;
                answer.innerText = dto[now].answer;
            }, 500);
            check = setTimeout(function () {
                card.classList.remove("nextshow");
                check = null;
            }, 1000);
        }
    }

    // knuth shuffle
    function shuffle(array) {
        for (let i = array.length - 1; i > 0; i--) {
            const rand = Math.floor(Math.random() * (i + 1));
            const temp = array[i];
            array[i] = array[rand];
            array[rand] = temp;
        }
    }
    shuffle(dto);

    // 초기 로딩
    category.innerText = "(" + categoryMap.get(dto[now].cid) + ")";
    text.innerText = dto[now].question;
    question.innerText = dto[now].question;
    answer.innerText = dto[now].answer;

    // 끝내기 다이얼로그
    const closeModal = document.getElementById("modal-close");
    const finish = document.getElementById("finish");
    finish.addEventListener('click', () => {
        closeModal.style.display = "block";
    });

    const cancel = document.querySelector('.cancel');
    cancel.addEventListener('click', () => {
        closeModal.style.display = "none";
    });

    const end = document.querySelector('.end');
    end.addEventListener('click', () => {
        window.location = "/card";
    });

    // 카드 추가 이벤트
    function newCardHandler(event) {
        // 현재 리스트 조건에 맞는지 체크 후 추가
        for (let i = 0; i < keywords.length; i++) {
            if (keywords[i] == event.detail.cid) {
                max += 1;
                // 현재 위치 뒤에 무작위 삽입
                dto.splice(Math.floor(Math.random() * (dto.length - now)) + now + 1, 0, event.detail);
                counter.innerText = "(문항 수 : " + dto.length + ")";
                break
            }
        }
    }
    document.addEventListener('newcard', newCardHandler);
}