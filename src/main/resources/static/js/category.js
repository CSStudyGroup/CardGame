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

    // 카테고리 해싱
    let categoryMap = new Map();
    for (let i = 0; i < categoryDto.length; i++) {
        categoryMap.set(categoryDto[i].cid, categoryDto[i].cname);
    }

    // 카테고리 표시
    const categoryname = document.getElementById("directory");
    categoryname.innerText = "> " + categoryMap.get(dto[0].cid);

    // 각 종 elements
    const cardTitle = document.getElementById("card-title");
    const text = document.getElementById("card-text");
    const question = document.getElementById("card-question");
    const answer = document.getElementById("card-answer");

    // 카드 클릭 애니메이션
    var check = null;
    card.addEventListener("click", function () {
        if (check == null){
            card.classList.add("rotate")
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

    // 현재 문제 및 전체문제 수 체크
    const nowele = document.getElementById("now");
    var now = 1;
    document.getElementById("total").innerText = String(dto.length);
    var total = dto.length;

    // 처음 로딩
    text.innerText = dto[now -1].question;
    question.innerText = dto[now - 1].question;
    answer.innerText = dto[now - 1].answer;

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
        if (check == null) {
            if (now == 1) {
                alert("첫 카드입니다.")
            }
            else {
                now -= 1;
                nowele.innerText = now;
                card.classList.add("left2right");
                setTimeout(function () {
                    //텍스트 변환
                    cardTitle.innerText = "Q.";
                    text.innerText = dto[now -1].question;
                    question.innerText = dto[now - 1].question;
                    answer.innerText = dto[now - 1].answer;
                }, 500);
                check = setTimeout(function () {
                    card.classList.remove("left2right")
                    check = null;
                }, 1000);
            }
        }
    }

    // 다음 문제 진행 요청
    const next = document.getElementById("next");
    next.addEventListener("click", nextMotion);
    function nextMotion() {
        if (check == null) {
            if (now == total) {
                alert("마지막 카드입니다.")
            }
            else {
                now += 1;
                nowele.innerText = now;
                card.classList.add("right2left");
                setTimeout(function () {
                    //텍스트 변환
                    cardTitle.innerText = "Q.";
                    text.innerText = dto[now -1].question;
                    question.innerText = dto[now - 1].question;
                    answer.innerText = dto[now - 1].answer;
                }, 500);
                check = setTimeout(function () {
                    card.classList.remove("right2left")
                    check = null;
                }, 1000);
            }
        }
    }

    // 카드 추가 이벤트
    function newCardHandler(event) {
        if (dto[0].cid == event.detail.cid) {
            total += 1
            document.getElementById("total").innerText = String(total);
            dto.push(event.detail);
        }
    }
    document.addEventListener('newcard', newCardHandler);
}