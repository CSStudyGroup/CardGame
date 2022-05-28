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
    for (let i = 0; i < categoryDtoList.length; i++) {
        categoryMap.set(categoryDtoList[i].id, categoryDtoList[i].name);
    }

    // 문항 수 표시
    const counter = document.querySelector(".page-title");
    counter.firstElementChild.innerText = "Interview (문항 수 : " + dto.length + ")";

    // 카드 클릭 애니메이션
    const cardTitle = document.querySelector(".card-title");
    const question = document.querySelector(".card-question");
    const answer = document.querySelector(".card-answer");
    const category = document.querySelector(".card-category");
    let check = null;
    card.addEventListener("click", function () {
        if (check == null && !codeBoxClick){
            card.classList.add("rotate");
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

    // 문항 수 관련 변수==
    let now = 0

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

            card.classList.add("prev-show");
            setTimeout(function () {
                cardTitle.innerText = "Q.";
                category.innerText = "(" + categoryMap.get(dto[now].cid) + ")";
                question.style.display = "block";
                answer.style.display = "none";
                answer.scrollTo(0,0);
                question.innerText = dto[now].question;
                answer.innerHTML = marked.parse(dto[now].answer);
                adjustCodeBox();
            }, 500);
            check = setTimeout(function () {
                card.classList.remove("prev-show");
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

            card.classList.add("next-show");
            setTimeout(function () {
                cardTitle.innerText = "Q.";
                category.innerText = "(" + categoryMap.get(dto[now].cid) + ")";
                question.style.display = "block";
                answer.style.display = "none";
                answer.scrollTo(0,0);
                question.innerText = dto[now].question;
                answer.innerHTML = marked.parse(dto[now].answer);
                adjustCodeBox();
            }, 500);
            check = setTimeout(function () {
                card.classList.remove("next-show");
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
    question.innerText = dto[now].question;
    answer.innerHTML = marked.parse(dto[now].answer);
    adjustCodeBox();

    // 끝내기 다이얼로그
    const closeModal = document.getElementById("modal-close");
    const finish = document.getElementById("finish");
    finish.addEventListener('click', () => {
        closeModal.style.display = "flex";
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
                // 현재 위치 뒤에 무작위 삽입
                dto.splice(Math.floor(Math.random() * (dto.length - now)) + now + 1, 0, event.detail);
                counter.firstElementChild.innerText = "Interview (문항 수 : " + dto.length + ")";
                break
            }
        }
    }
    document.addEventListener('newcard', newCardHandler);

    // 코드 박스 드래그 체킹용
    let codeBoxClick = false;
    let isMouseDown = false;
    let startX, scrollLeft;

    function adjustCodeBox() {
        const codeBox = document.querySelectorAll("code");

        for (let i = 0; i < codeBox.length; i++) {
            // 복사 버튼 생성
            let new_button = document.createElement("button");
            new_button.setAttribute("class", "code-copy");
            new_button.innerText = "코드 복사";
            new_button.addEventListener("click", (e) => {
                e.stopPropagation();
                navigator.clipboard.writeText(codeBox[i].innerText)
                    // 성공인 경우
                    .then(() => {
                        alert("클립보드에 복사 성공");
                    })
                    // 실패인 경우
                    .catch(err => {
                        alert("클립보드에 복사 실패");
                        console.log('클립보드에 복사 실패', err);
                    })
            })
            codeBox[i].parentElement.append(new_button);

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

            // 드래그를 통한 이동 처리
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
    }

    // 코드 박스 드래그 끝 처리
    document.addEventListener('mouseup', () => {
        setTimeout(function () {
            codeBoxClick = false;
        }, 1);
    })
}