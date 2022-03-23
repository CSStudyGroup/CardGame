// 카드 추가를 위한 element
const category = document.getElementById("insertCategory");
const insertModal = document.getElementById("insertModal");
const insertModalOverlay = document.getElementById("insertModalOverlay");
const insertCategory = document.getElementById("insertCategory");
const insertTags = document.getElementById("insertTags");
const insertQuestion = document.getElementById("insertQuestion");
const insertAnswer = document.getElementById("insertAnswer");

function insert() {
    insertCategory.value = "none";
    insertQuestion.value = "";
    insertAnswer.value = "";
    insertTags.value = "";
    insertModal.style.display = "block";
    insertModalOverlay.style.display = "flex";
}

function insertModalSubmit(){
    console.log(insertModal);
    insertModal.style.display = "none";
    insertModalOverlay.style.display = "none";

    // AJAX 수정 요청
    let httpRequest = new XMLHttpRequest();
    httpRequest.onreadystatechange = postInsertCard;

    function postInsertCard(){
        if (httpRequest.readyState === XMLHttpRequest.DONE) {
            if (httpRequest.status === 200) {
                if (httpRequest.response == 1) {
                    alert("삽입 성공");
                }
                else {
                    alert("삽입 실패");
                }
            } else {
                alert('Request Error!');
            }
        }
    }
    httpRequest.open('POST',
        '/card/cardInsert'
        + "?cid=" + insertCategory.value
        + "&question=" + insertQuestion.value
        + "&answer=" + insertAnswer.value
        + "&tags=" + insertTags.value );
    httpRequest.send();
}

function insertModalClose(){
    insertModal.style.display = "none";
    insertModalOverlay.style.display = "none";
}


// 인터뷰 링크
const interviewDialog = document.getElementById("dialog-interview");

function interview() {
    if (typeof interviewDialog.showModal === 'function') {
        interviewDialog.showModal();
    } else {
        alert("현재 브라우저는 해당 기능을 지원하지 않습니다.")
    }
}