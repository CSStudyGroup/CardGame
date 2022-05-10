window.onload = function(){
    // 카드가 0개 일 경우 예외 처리
    if (dto.length === 0) {
        alert("선택 된 카테고리의 카드가 없습니다.\n메인으로 돌아갑니다.");
        window.location = "/card";
    }

    // 모바일에 따른 변화
    const prevShape = document.querySelector(".prev");
    const nextShape = document.querySelector(".next");
    const card = document.querySelector(".container");
    const flexContent = document.querySelector(".flex-content");
    function isMobile(){
        return /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent);
    }

    if (isMobile()) {
        flexContent.style.justifyContent = "center";
        prevShape.style.display = "none";
        nextShape.style.display = "none";
        card.style.width = "calc(100vw - 140px)";
    }

    // 카테고리 해싱
    let categoryMap = new Map();
    for (let i = 0; i < categoryDto.length; i++) {
        categoryMap.set(categoryDto[i].id, categoryDto[i].name);
    }

    // 카테고리 표시
    const pageTitle = document.querySelector(".page-title");
    pageTitle.innerText = "Category > " + categoryMap.get(dto[0].cid);

    // 각 종 elements
    const cardTitle = document.querySelector(".card-title");
    const question = document.querySelector(".card-question");
    const answer = document.querySelector(".card-answer");

    // 카드 클릭 애니메이션
    let check = null;
    card.addEventListener("click", function () {
        if (check == null && !codeBoxClick){
            card.classList.add("rotate")
            setTimeout(function () {
                if (cardTitle.innerText === "Q.") {
                    cardTitle.innerText = "A.";
                    question.style.display = "none";
                    answer.style.display = "block";
                    answer.scrollTo(0,0);
                }
                else {
                    cardTitle.innerText = "Q.";
                    question.style.display = "block";
                    answer.style.display = "none";
                }
            }, 500);
            check = setTimeout(function () {
                card.classList.remove("rotate")
                check = null;
            }, 1000);
        }
    })

    // 현재 문제 및 전체문제 수 체크
    const nowElement = document.getElementById("now");
    let now = 1;
    document.getElementById("total").innerText = String(dto.length);
    let total = dto.length;

    // 처음 로딩
    question.innerText = dto[now - 1].question;
    answer.innerHTML = marked.parse(dto[now - 1].answer);

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
            if (now === 1) {
                alert("첫 카드입니다.")
            }
            else {
                now -= 1;
                nowElement.innerText = now;
                card.classList.add("prev-show");
                setTimeout(function () {
                    //텍스트 변환
                    cardTitle.innerText = "Q.";
                    question.style.display = "block";
                    answer.style.display = "none";
                    answer.scrollTo(0,0);
                    question.innerText = dto[now - 1].question;
                    answer.innerHTML = marked.parse(dto[now - 1].answer);
                }, 500);
                check = setTimeout(function () {
                    card.classList.remove("prev-show")
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
            if (now === total) {
                alert("마지막 카드입니다.")
            }
            else {
                now += 1;
                nowElement.innerText = now;
                card.classList.add("next-show");
                setTimeout(function () {
                    //텍스트 변환
                    cardTitle.innerText = "Q.";
                    question.style.display = "block";
                    answer.style.display = "none";
                    answer.scrollTo(0,0);
                    question.innerText = dto[now - 1].question;
                    answer.innerHTML = marked.parse(dto[now - 1].answer);
                }, 500);
                check = setTimeout(function () {
                    card.classList.remove("next-show")
                    check = null;
                }, 1000);
            }
        }
    }

    // 카드 추가 이벤트
    function newCardHandler(event) {
        if (dto[0].cid === event.detail.cid) {
            total += 1
            document.getElementById("total").innerText = String(total);
            dto.push(event.detail);
        }
    }
    document.addEventListener('newcard', newCardHandler);

    // 코드 박스 드래그 체킹용
    const codeBox = document.querySelectorAll("code");
    let codeBoxClick = false;
    let isMouseDown = false;
    let startX, scrollLeft;

    for (let i = 0; i < codeBox.length; i++) {

        // 드래그 방지
        codeBox[i].onselectstart = new Function("return false");

        // 클릭 방지
        codeBox[i].addEventListener('click', (e) => {
            if (Math.abs((e.pageX - codeBox[i].offsetLeft) - startX) > 10) {
                e.stopPropagation();
            }
            else {
                codeBoxClick = false;
            }
        })

        codeBox[i].addEventListener('mousedown', (e) => {
            codeBoxClick = true;
            isMouseDown = true;
            codeBox[i].classList.add('active');

            startX = e.pageX - codeBox[i].offsetLeft;
            scrollLeft = codeBox[i].scrollLeft;
        });

        codeBox[i].addEventListener('mouseleave', () => {
            isMouseDown = false;
            codeBox[i].classList.remove('active');
        });

        codeBox[i].addEventListener('mouseup', () => {
            isMouseDown = false;
            codeBox[i].classList.remove('active');
        });

        codeBox[i].addEventListener('mousemove', (e) => {
            if (!isMouseDown) return;
            const x = e.pageX - codeBox[i].offsetLeft;
            const walk = (x - startX) * 1;
            codeBox[i].scrollLeft = scrollLeft - walk;
        });
    }

    // 코드 박스 드래그 끝 처리
    document.addEventListener('mouseup', () => {
        setTimeout(function () {
            codeBoxClick = false;
        }, 1);
    })

}