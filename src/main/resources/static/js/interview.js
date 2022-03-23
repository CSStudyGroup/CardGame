window.onload = function(){
    // 카드가 0개 일 경우 예외 처리
    if (dto.length == 0) {
        alert("선택 된 카테고리의 카드가 없습니다.\n메인으로 돌아갑니다.");
        window.location = "/card";
    }

    // 문항 수 표시
    const headTitle = document.querySelector(".headTitle");
    headTitle.innerText = "Interview (총 문항 수 : " + dto.length + ")";

    // 카드 클릭 애니메이션
    const card = document.querySelector(".container");
    const text = document.getElementById("text");
    const question = document.getElementById("question");
    const answer = document.getElementById("answer");
    var check = null;
    card.addEventListener("click", function () {
        if (check == null){
            card.classList.add("rotate");
            setTimeout(function () {
                if (text.innerText == question.innerText) {
                    text.innerText = answer.innerText;
                }
                else {
                    text.innerText = question.innerText;
                }
            }, 500);
            check = setTimeout(function () {
                card.classList.remove("rotate")
                check = null;
            }, 1000);
        }
    })

    // 다음 문제 진행 요청
    const min = 0
    const max = dto.length - 1;
    const next = document.getElementById("next");
    next.addEventListener("click", function () {

        if (check == null){
            // 랜덤 추출
            var rand = Math.floor(Math.random() * (max - min + 1)) + min;
            console.log(rand);

            card.classList.add("nextshow");
            setTimeout(function () {
                text.innerText = dto[rand].question;
                question.innerText = dto[rand].question;
                answer.innerText = dto[rand].answer;
            }, 1000);
            check = setTimeout(function () {
                card.classList.remove("nextshow");
                check = null;
            }, 2000);
        }
    });

    // 초기 로딩
    var rand = Math.floor(Math.random() * (max - min + 1)) + min;
    text.innerText = dto[rand].question;
    question.innerText = dto[rand].question;
    answer.innerText = dto[rand].answer;

    // 끝내기 다이얼로그
    const dialog = document.querySelector('.dialog');
    const finish = document.getElementById("finish");
    finish.addEventListener('click', () => {
        if (typeof dialog.showModal === 'function') {
            dialog.showModal();
        } else {
            alert("현재 브라우저는 해당 기능을 지원하지 않습니다.")
        }
    });

    const cancel = document.querySelector('.cancel');
    cancel.addEventListener('click', () => {
        dialog.close();
    });

    const end = document.querySelector('.end');
    end.addEventListener('click', () => {
        window.location = "/card";
    });
}