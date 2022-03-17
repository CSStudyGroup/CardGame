window.onload = function(){
    // 카드 클릭 애니메이션
    const card = document.querySelector(".container");
    const text = document.getElementById("text");
    const question = document.getElementById("question");
    const answer = document.getElementById("answer");
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
            }, 500);
            check = setTimeout(function () {
                card.classList.remove("rotate")
                check = null;
            }, 1000);
        }
    })

    // 다음 문제 진행 요청
    const next = document.getElementById("next");
    next.addEventListener("click", function () {
        alert("next");
    });
}