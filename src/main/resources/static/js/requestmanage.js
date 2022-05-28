// 메뉴 선택
const menuItems = document.querySelectorAll(".menu-item");
const containerTitle = document.querySelector(".container-title");
const containerSearch = document.querySelector(".container-search");
const containerSearchButton = document.querySelector(".container-search-button");
const containerAddButton = document.querySelector(".container-add-button");
const table = document.querySelector(".table");

function moveCardAdd(temp) {

    // 반환 결과물에 따른 표 작성
    table.innerHTML = "";

    let new_head = document.createElement("tr");
    new_head.setAttribute("class", "head");

    let new_th1 = document.createElement("th");
    new_th1.setAttribute("width", "10%");
    new_th1.innerText = "ID";
    new_head.appendChild(new_th1);

    let new_th2 = document.createElement("th");
    new_th2.setAttribute("width", "10%");
    new_th2.innerText = "Card ID";
    new_head.appendChild(new_th2);

    let new_th3 = document.createElement("th");
    new_th3.setAttribute("width", "20%");
    new_th3.innerText = "Request ID";
    new_head.appendChild(new_th3);

    let new_th4 = document.createElement("th");
    new_th4.setAttribute("width", "40%");
    new_th4.innerText = "Title";
    new_head.appendChild(new_th4);

    let new_th5 = document.createElement("th");
    new_th5.setAttribute("width", "20%");
    new_th5.innerText = "처리 여부";
    new_head.appendChild(new_th5);

    table.appendChild(new_head);

    for (let i = 0; i < 3; i++) {
        let new_tableBody = document.createElement("tr");
        new_tableBody.setAttribute("class", "tableBody");

        let new_td1 = document.createElement("td");
        new_td1.setAttribute("class", "underline");
        new_td1.innerText = "1";
        new_tableBody.appendChild(new_td1);

        let new_td2 = document.createElement("td");
        new_td2.setAttribute("class", "underline");
        new_td2.innerText = "423";
        new_tableBody.appendChild(new_td2);

        let new_td3 = document.createElement("td");
        new_td3.setAttribute("class", "underline");
        new_td3.innerText = "useridasdf";
        new_tableBody.appendChild(new_td3);

        let new_td4 = document.createElement("td");
        new_td4.setAttribute("class", "underline clickable");
        new_td4.setAttribute("onclick", "cardView(id)");
        new_td4.innerText = "content";
        new_tableBody.appendChild(new_td4);

        let new_td5 = document.createElement("td");
        new_td5.setAttribute("class", "underline");
        new_td5.innerText = "X";
        new_tableBody.appendChild(new_td5);

        table.appendChild(new_tableBody);
    }

    containerTitle.innerText = "카드 추가 요청"
    for (let i = 0; i < menuItems.length; i++) {
        if (menuItems[i].innerText === "카드 추가 요청") {
            menuItems[i].style.color = "red"
        }
        else {
            menuItems[i].style.color = "black"
        }
    }
    containerSearch.style.display = "block";
    containerSearchButton.style.display = "block";
    containerAddButton.style.marginLeft = "0px";
}

function moveSubRequest(temp) {

    // 반환 결과물에 따른 표 작성
    table.innerHTML = "";

    let new_head = document.createElement("tr");
    new_head.setAttribute("class", "head");

    let new_th1 = document.createElement("th");
    new_th1.setAttribute("width", "10%");
    new_th1.innerText = "ID";
    new_head.appendChild(new_th1);

    let new_th2 = document.createElement("th");
    new_th2.setAttribute("width", "20%");
    new_th2.innerText = "Request ID";
    new_head.appendChild(new_th2);

    let new_th3 = document.createElement("th");
    new_th3.setAttribute("width", "50%");
    new_th3.innerText = "Title";
    new_head.appendChild(new_th3);

    let new_th4 = document.createElement("th");
    new_th4.setAttribute("width", "20%");
    new_th4.innerText = "처리 여부";
    new_head.appendChild(new_th4);

    table.appendChild(new_head);

    for (let i = 0; i < 5; i++) {
        let new_tableBody = document.createElement("tr");
        new_tableBody.setAttribute("class", "tableBody");

        let new_td1 = document.createElement("td");
        new_td1.setAttribute("class", "underline");
        new_td1.innerText = "1";
        new_tableBody.appendChild(new_td1);

        let new_td2 = document.createElement("td");
        new_td2.setAttribute("class", "underline");
        new_td2.innerText = "useridasdf";
        new_tableBody.appendChild(new_td2);

        let new_td3 = document.createElement("td");
        new_td3.setAttribute("class", "underline clickable");
        new_td3.setAttribute("onclick", "subView(id)");
        new_td3.innerText = "content";
        new_tableBody.appendChild(new_td3);

        let new_td4 = document.createElement("td");
        new_td4.setAttribute("class", "underline");
        new_td4.innerText = "X";
        new_tableBody.appendChild(new_td4);

        table.appendChild(new_tableBody);
    }

    containerTitle.innerText = "기타 요청"
    for (let i = 0; i < menuItems.length; i++) {
        if (menuItems[i].innerText === "기타 요청") {
            menuItems[i].style.color = "red"
        }
        else {
            menuItems[i].style.color = "black"
        }
    }
    containerSearch.style.display = "none";
    containerSearchButton.style.display = "none";
    containerAddButton.style.marginLeft = "auto";
}

let temp;

function ajaxCardAddList() {
    alert("request server CardAddList");
    temp = 0;
    moveCardAdd(temp);
}

function ajaxSubRequestList() {
    alert("request server SubRequestList");
    temp = 0;
    moveSubRequest(temp);
}

// 카드 보기
const cardViewModal = document.getElementById("modal-cardview");
const cardViewTitleText = document.getElementById("modal-cardview-title-text");
const cardViewCategory = document.getElementById("modal-cardview-content-category");
const cardViewRequestID = document.getElementById("modal-cardview-content-requestID")
const cardViewTags = document.getElementById("modal-cardview-content-tags");
const cardViewQuestion = document.getElementById("modal-cardview-content-question");
const cardViewAnswer = document.getElementById("modal-cardview-content-answer");

function cardView(id) {
    cardViewTitleText.innerText = "3" + "번 요청 내용";
    cardViewCategory.innerText = "(" + "category" + ")";
    cardViewRequestID.innerText = "(" + "Request ID" + ")";
    cardViewTags.value = "tags";
    cardViewQuestion.value = "Question";
    cardViewAnswer.value = "Answer";
    cardViewModal.style.display = "flex";
}

function cardViewModalClose(){
    cardViewModal.style.display = "none";
}

function cardAccept() {
    alert("card accept!");
    cardViewModalClose();
}

function cardReject() {
    alert("card reject");
    cardViewModalClose();
}

// 서브 보기
const subViewModal = document.getElementById("modal-subview");
const subViewTitleText = document.getElementById("modal-subview-title-text");
const subViewRequestID = document.getElementById("modal-subview-content-requestID");
const subViewTitle = document.getElementById("modal-subview-content-title");
const subViewContent = document.getElementById("modal-subview-content-content");

function subView(id) {
    subViewTitleText.innerText = "3" + "번 요청 내용";
    subViewRequestID.innerText = "(" + "request id" + ")";
    subViewTitle.value = "Title";
    subViewContent.value = "Content";
    subViewModal.style.display = "flex";
}

function subViewModalClose(){
    subViewModal.style.display = "none";
}

function subAccept() {
    alert("sub accept!");
}

function subReject() {
    alert("sub reject");
}

// 서브 글쓰기
function writeRequest() {
    if (containerTitle.innerText === "카드 추가 요청") {
        insert();
    }
    else {
        if (role !== 'guest') {
            subWrite("asdf");
        }
        else {
            alert("로그인이 필요한 서비스입니다.");
            location.href = "/login";
            return;
        }
    }
}

const subWriteModal = document.getElementById("modal-subwrite");
const subWriteTitle = document.getElementById("modal-subwrite-content-title");
const subWriteContent = document.getElementById("modal-subwrite-content-content");

function subWrite(id) {
    subWriteModal.style.display = "flex";
}

function subWriteModalClose(){
    subWriteModal.style.display = "none";
}


// 카드 검색 기능
function cardRequestSearch() {
    alert(containerSearch.value + " search!");
    temp = 0;
    moveCardAdd(temp);
}

window.onload = function () {
    // 초기 설정
    moveCardAdd();

    // role에 따른 설정

}
