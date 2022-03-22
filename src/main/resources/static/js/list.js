// AJAX용 request
var httpRequest;

// 본문 보기
const viewModal = document.getElementById("viewModal");
const viewId = document.getElementById("viewId");
const viewCategory = document.getElementById("viewCategory");
const viewTags = document.getElementById("viewTags");
const viewQuestion = document.getElementById("viewQuestion");
const viewAnswer = document.getElementById("viewAnswer");
var check = false;

function text(index) {
    if (!check) {
        viewId.innerText = dto[index].id;
        viewCategory.innerText = dto[index].category;
        viewTags.innerText = dto[index].tags;
        viewQuestion.innerText = dto[index].question;
        viewAnswer.innerText = dto[index].answer;
        viewModal.style.display = "block";
        check = true;
    }
}

function viewModalClose(){
    viewModal.style.display = "none";
    check = false;
}

// 수정하기
const updateModal = document.getElementById("updateModal");
const updateId = document.getElementById("updateId");
const updateCategory = document.getElementById("updateCategory");
const updateTags = document.getElementById("updateTags");
const updateQuestion = document.getElementById("updateQuestion");
const updateAnswer = document.getElementById("updateAnswer");

function update(index){
    if (!check) {
        updateId.innerText = dto[index].id;
        updateCategory.value = dto[index].category;
        updateTags.value = dto[index].tags;
        updateQuestion.value = dto[index].question;
        updateAnswer.value = dto[index].answer;
        updateModal.style.display = "block";
        check = true;
    }
}

function updateModalSubmit(){
    updateModal.style.display = "none";

    // AJAX 수정 요청
    httpRequest = new XMLHttpRequest();
    httpRequest.onreadystatechange = postUpdateCard;

    function postUpdateCard(){
        if (httpRequest.readyState === XMLHttpRequest.DONE) {
            if (httpRequest.status === 200) {
                if (httpRequest.response == 1) {
                    alert("수정 성공");
                    // 해당 줄 내용 수정
                }
                else {
                    alert("수정 실패");
                }
            } else {
                alert('Request Error!');
            }
        }
    }
    httpRequest.open('POST',
        '/card/cardUpdate'
        + "?id=" + updateId.innerText
        + "&category=" + updateCategory.value
        + "&question=" + updateQuestion.value
        + "&answer=" + updateAnswer.value
        + "&tags=" + updateTags.value );
    httpRequest.send();

    check = false;
}

function updateModalClose(){
    updateModal.style.display = "none";
    check = false;
}

// 삭제
const dialog = document.querySelector('.dialog');
const caution = document.querySelector(".caution");
var target = -1;
function del(index){
    if (!check) {
        caution.textContent = dto[index].id + "번을 정말로 삭제하시겠습니까?";
        target = index;
        if (typeof dialog.showModal === 'function') {
            dialog.showModal();
        } else {
            alert("현재 브라우저는 해당 기능을 지원하지 않습니다.")
        }
        check = true
    }
}

const cancel = document.querySelector('.cancel');
cancel.addEventListener('click', () => {
    dialog.close();
    check = false;
});

const remove = document.querySelector('.remove');
remove.addEventListener('click', () => {
    // AJAX 삭제 요청
    if (target != -1) {
        httpRequest = new XMLHttpRequest();
        httpRequest.onreadystatechange = postUpdateCard;

        function postUpdateCard(){
            if (httpRequest.readyState === XMLHttpRequest.DONE) {
                if (httpRequest.status === 200) {
                    if (httpRequest.response == 1) {
                        alert("삭제 성공");
                        // 리스트에서 해당 줄 제거
                    }
                    else {
                        alert("삭제 실패");
                    }
                } else {
                    alert('Request Error!');
                }
            }
        }
        httpRequest.open('POST',
            '/card/cardDelete'
            + "?id=" + dto[target].id
            + "&category=" + dto[target].category
            + "&question=" + dto[target].question
            + "&answer=" + dto[target].answer
            + "&tags=" + dto[target].tags);
        console.log('/card/cardDelete'
            + "?id=" + dto[target].id
            + "&category=" + dto[target].category
            + "&question=" + dto[target].question
            + "&answer=" + dto[target].answer
            + "&tags=" + dto[target].tags);
        httpRequest.send();
        target = -1;
    }
    dialog.close();
    check = false;
});

window.onload = function(){
    const category = document.getElementById("updateCategory");

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