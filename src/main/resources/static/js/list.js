// AJAX용 request
var listHttpRequest;

// 해싱용 맵
let categoryMap;

// 본문 보기
var target = -1;
const viewModal = document.getElementById("viewModal");
const viewId = document.getElementById("viewId");
const viewCategory = document.getElementById("viewCategory");
const viewTags = document.getElementById("viewTags");
const viewQuestion = document.getElementById("viewQuestion");
const viewAnswer = document.getElementById("viewAnswer");

function text(id) {
    for (let i = 0; i < dto.length; i++) {
        if (dto[i].id == id) {
            viewId.innerText = dto[i].id;
            viewCategory.innerText = categoryMap.get(dto[i].cid);
            viewTags.innerText = dto[i].tags;
            viewQuestion.innerText = dto[i].question;
            viewAnswer.innerText = dto[i].answer;
            viewModal.style.display = "block";
            break;
        }
    }
}

function viewModalClose(){
    viewModal.style.display = "none";
}

// 수정하기
const updateModal = document.getElementById("updateModal");
const updateId = document.getElementById("updateId");
const updateCategory = document.getElementById("updateCategory");
const updateTags = document.getElementById("updateTags");
const updateQuestion = document.getElementById("updateQuestion");
const updateAnswer = document.getElementById("updateAnswer");

function update(id){
    for (let i = 0; i < dto.length; i++) {
        if (dto[i].id == id) {
            target = i;
            updateId.innerText = dto[i].id;
            updateCategory.value = dto[i].cid;
            updateTags.value = dto[i].tags;
            updateQuestion.value = dto[i].question;
            updateAnswer.value = dto[i].answer;
            updateModal.style.display = "block";
            break;
        }
    }
}

function updateModalSubmit(){
    updateModal.style.display = "none";

    // AJAX 수정 요청
    listHttpRequest = new XMLHttpRequest();
    listHttpRequest.onreadystatechange = postUpdateCard;

    function postUpdateCard(){
        if (listHttpRequest.readyState === XMLHttpRequest.DONE) {
            if (listHttpRequest.status === 200) {
                if (listHttpRequest.response == 1) {
                    alert("수정 성공");
                    // dto 및 테이블 내용 수정
                    const temp = document.querySelectorAll(".tableBody")[target];

                    console.log(dto[target]);
                    // dto 수정
                    dto[target].cid = parseInt(updateCategory.value);
                    dto[target].tags = updateTags.value;
                    dto[target].question = updateQuestion.value;
                    dto[target].answer = updateAnswer.value;

                    console.log(dto[target]);

                    // 테이블 수정
                    temp.children[1].innerText = categoryMap.get(parseInt(updateCategory.value));
                    temp.children[2].innerText = updateQuestion.value;
                    temp.children[3].innerText = updateTags.value;
                }
                else {
                    alert("수정 실패");
                }
            } else {
                alert('Request Error!');
            }
            target = -1;
        }
    }
    listHttpRequest.open('POST',
        '/card/cardUpdate'
        + "?id=" + updateId.innerText
        + "&cid=" + updateCategory.value
        + "&question=" + updateQuestion.value
        + "&answer=" + updateAnswer.value
        + "&tags=" + updateTags.value );
    listHttpRequest.send();
}

function updateModalClose(){
    updateModal.style.display = "none";
}

// 삭제
const dialog = document.querySelector('.dialog');
const deleteCaution = document.querySelector(".delete-caution");
function del(id){
    for (let i = 0; i < dto.length; i++) {
        if (dto[i].id == id) {
            target = i;
            break;
        }
    }
    deleteCaution.textContent = dto[target].id + "번을 정말로 삭제하시겠습니까?";
    if (typeof dialog.showModal === 'function') {
        dialog.showModal();
    } else {
        alert("현재 브라우저는 해당 기능을 지원하지 않습니다.")
    }
}

const cancel = document.querySelector('.cancel');
cancel.addEventListener('click', () => {
    dialog.close();
});

const remove = document.querySelector('.remove');
remove.addEventListener('click', () => {
    // AJAX 삭제 요청
    if (target != -1) {
        listHttpRequest = new XMLHttpRequest();
        listHttpRequest.onreadystatechange = postUpdateCard;

        function postUpdateCard(){
            if (listHttpRequest.readyState === XMLHttpRequest.DONE) {
                if (listHttpRequest.status === 200) {
                    if (listHttpRequest.response == 1) {
                        alert("삭제 성공");
                        window.location.reload();
                    }
                    else {
                        alert("삭제 실패");
                    }
                } else {
                    alert('Request Error!');
                }
                dialog.close();
                target = -1;
            }
        }
        listHttpRequest.open('POST',
            '/card/cardDelete'
            + "?id=" + dto[target].id
            + "&cid=" + dto[target].cid
            + "&question=" + dto[target].question
            + "&answer=" + dto[target].answer
            + "&tags=" + dto[target].tags);
        listHttpRequest.send()
    }
});

window.onload = function(){
    // 0개 대응
    if (dto.length == 0) {
        const main = document.querySelector(".table");
        main.innerHTML = "<br><br><br><br><br><br><br><br>검색 결과가 없습니다.<br><br>다른 검색어로 검색해주세요";
    }

    // 카테고리 해싱
    categoryMap = new Map();
    for (let i = 0; i < categoryDtoList.length; i++) {
        categoryMap.set(categoryDtoList[i].cid, categoryDtoList[i].cname);
    }

    // 카테고리 변환
    const categoryCids = document.querySelectorAll(".category");
    for (let i = 0; i < categoryCids.length; i++) {
        categoryCids[i].innerText = categoryMap.get(parseInt(categoryCids[i].innerText));
    }

    // 카드 추가 이벤트
    function newCardHandler(event) {
        // 현재 리스트 조건에 맞는지 체크 후 추가
        var flag = false;

        if (tag != "") {
            // 검색어 포함 태그 탐색
            if (event.detail.tags.includes(tag)) {
                flag = true;
            }
        }
        else if (question != "") {
            // 검색어 포함 질문 탐색
            if (event.detail.question.includes(question)) {
                flag = true;
            }
        }
        else if (cid != "") {
            if (event.detail.cid == cid) {
                flag = true;
            }
        }

        if (flag) {
            // 새로운 tr
            let new_tr = document.createElement("tr");
            new_tr.setAttribute("class", "tableBody");

            // id
            let new_id = document.createElement("td");
            new_id.textContent = event.detail.id;
            new_id.setAttribute("class", "id");
            new_tr.append(new_id);

            // category
            let new_category = document.createElement("td");
            new_category.textContent = categoryMap.get(event.detail.cid);
            new_category.setAttribute("class", "category");
            new_tr.append(new_category);

            // question
            let new_question = document.createElement("td");
            new_question.setAttribute("class", "question clickable");
            new_question.setAttribute("onclick", "text('" + event.detail.id + "')");
            new_question.textContent = event.detail.question;
            new_tr.append(new_question);

            // tags
            let new_tags = document.createElement("td");
            new_tags.textContent = event.detail.tags;
            new_tags.setAttribute("class", "tags");
            new_tr.append(new_tags);

            // update
            let new_update = document.createElement("td");
            new_update.setAttribute("class", "update");
            let new_update_img = document.createElement("img");
            new_update_img.setAttribute("class", "clickable");
            new_update_img.setAttribute("height", "30px");
            new_update_img.setAttribute("width", "auto");
            new_update_img.setAttribute("src", "/img/edit_image.png");
            new_update_img.setAttribute("onclick", "update('" + event.detail.id + "')");
            new_update.append(new_update_img);
            new_tr.append(new_update);

            // delete
            let new_delete = document.createElement("td");
            new_delete.setAttribute("class", "delete");
            let new_delete_img = document.createElement("img");
            new_delete_img.setAttribute("class", "clickable");
            new_delete_img.setAttribute("height", "30px");
            new_delete_img.setAttribute("width", "auto");
            new_delete_img.setAttribute("src", "/img/delete_image.png");
            new_delete_img.setAttribute("onclick", "del('" + event.detail.id + "')");
            new_delete.append(new_delete_img);
            new_tr.append(new_delete);

            // 새로운 줄 추가
            const table = document.querySelector(".table");
            table.children[0].append(new_tr);

            // dto 추가
            dto.push(event.detail);
        }
    }
    document.addEventListener('newcard', newCardHandler);
}