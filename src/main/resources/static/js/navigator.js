function interview() {
    console.log("interview button clicked");
}

function insert(){
    insertModal.style.display = "block";
}

function insertModalSubmit(){
    console.log(insertModal);
    insertModal.style.display = "none";

    // AJAX 수정 요청
    httpRequest = new XMLHttpRequest();
    httpRequest.onreadystatechange = postInsertCard;

    function postInsertCard(){
        if (httpRequest.readyState === XMLHttpRequest.DONE) {
            if (httpRequest.status === 200) {
                if (httpRequest.response == 1) {
                    alert("삽입 성공");
                    // 해당 줄 내용 수정
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
        + "?category=" + insertCategory.value
        + "&question=" + insertQuestion.value
        + "&answer=" + insertAnswer.value
        + "&tags=" + insertTags.value );
    httpRequest.send();
}

function insertModalClose(){
    insertModal.style.display = "none";
    check = false;
}

window.onload = function(){
    const category = document.getElementById("insertCategory");
    const insertModal = document.getElementById("insertModal");
    const insertCategory = document.getElementById("insertCategory");
    const insertTags = document.getElementById("insertTags");
    const insertQuestion = document.getElementById("insertQuestion");
    const insertAnswer = document.getElementById("insertAnswer");

    httpRequest = new XMLHttpRequest();
    httpRequest.onreadystatechange = getCategoryList;

    function getCategoryList(){
        if (httpRequest.readyState === XMLHttpRequest.DONE) {
            if (httpRequest.status === 200) {
                var results = httpRequest.response;
                for (const result of results) {
                    var op = new Option();
                    op.value = result.cname;
                    op.text = result.cname;
                    category.appendChild(op);
                }
            } else {
                alert('Request Error!');
            }
        }
    }

    httpRequest.open('POST', '/card/categoryList');
    httpRequest.responseType = "json";
    httpRequest.send();
}