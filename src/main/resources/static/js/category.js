window.onload = function(){
    // 카드가 0개 일 경우 예외 처리
    if (dto.length == 0) {
        alert("선택 된 카테고리의 카드가 없습니다.\n메인으로 돌아갑니다.");
        window.location = "/card";
    }

    // 카테고리 해싱
    let categoryMap = new Map();
    for (let i = 0; i < categoryDto.length; i++) {
        categoryMap.set(categoryDto[i].cid, categoryDto[i].cname);
    }

    // 카테고리 표시
    const categoryname = document.getElementById("directory");
    categoryname.innerText = "카테고리 > " + categoryMap.get(dto[0].cid);

    // 각 종 elements
    const card = document.querySelector(".container");
    const text = document.getElementById("text");
    const question = document.getElementById("question");
    const answer = document.getElementById("answer");

    // 카드 클릭 애니메이션
    var check = null;
    card.addEventListener("click", function () {
        if (check == null){
            card.classList.add("rotate")
            setTimeout(function () {
                if (text.innerText == question.innerText) {
                    text.innerText = answer.innerText;
                }
                else {
                    text.innerText = question.innerText;
                }
                check = null;
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
    const total = dto.length;

    // 처음 로딩
    text.innerText = dto[now -1].question;
    question.innerText = dto[now - 1].question;
    answer.innerText = dto[now - 1].answer;

    // 이전 문제 진행 요청
    const prev = document.getElementById("prev");
    prev.addEventListener("click", function () {
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
                    text.innerText = dto[now -1].question;
                    question.innerText = dto[now - 1].question;
                    answer.innerText = dto[now - 1].answer;
                }, 1000);
                check = setTimeout(function () {
                    card.classList.remove("left2right")
                    check = null;
                }, 2000);
            }
        }
    });

    // 다음 문제 진행 요청
    const next = document.getElementById("next");
    next.addEventListener("click", function () {
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
                    text.innerText = dto[now -1].question;
                    question.innerText = dto[now - 1].question;
                    answer.innerText = dto[now - 1].answer;
                }, 1000);
                check = setTimeout(function () {
                    card.classList.remove("right2left")
                    check = null;
                }, 2000);
            }
        }
    });
}