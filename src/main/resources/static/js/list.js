// AJAX용 request
let listHttpRequest;

// 카테고리 해싱
let categoryMap = new Map();
for (let i = 0; i < categoryDtoList.length; i++) {
    categoryMap.set(categoryDtoList[i].id, categoryDtoList[i].name);
}

// 본문 보기
let target = -1;
const viewModal = document.getElementById("modal-view");
const modalViewTitleText = document.getElementById("modal-view-title-text");
const viewCategory = document.getElementById("modal-view-content-category");
const viewTags = document.getElementById("modal-view-content-tags");
const viewQuestion = document.getElementById("modal-view-content-question");
const viewAnswer = document.getElementById("modal-view-content-answer");

function text(id) {
    for (let i = 0; i < dto.length; i++) {
        if (dto[i].id === parseInt(id)) {
            modalViewTitleText.innerText = dto[i].id + "번 카드 내용";
            viewCategory.innerText = "(" + categoryMap.get(dto[i].cid) + ")";
            viewTags.value = dto[i].tags;
            viewQuestion.value = dto[i].question;
            viewAnswer.value = dto[i].answer;
            viewModal.style.display = "flex";
            break;
        }
    }
}

function viewModalClose(){
    viewModal.style.display = "none";
}

// 수정하기
const updateModal = document.getElementById("modal-update");
const modalUpdateTitleText = document.getElementById("modal-update-title-text");
const updateCategory = document.getElementById("modal-update-content-category-select");
const updateTags = document.getElementById("modal-update-content-tags");
const updateQuestion = document.getElementById("modal-update-content-question");
const updateAnswer = document.getElementById("modal-update-content-answer");

updateCategory.addEventListener('click', (e) => {
    e.stopPropagation();
})

updateModal.addEventListener('click', () => {
    updateCategory.classList.remove('sb-option-open');
});

function update(id){
    for (let i = 0; i < dto.length; i++) {
        if (dto[i].id === parseInt(id)) {
            target = i;
            modalUpdateTitleText.innerText = dto[i].id + "번 카드 수정";
            selectOption("modal-update-content-category-select", categoryMap.get(dto[i].cid), dto[i].cid)
            updateTags.value = dto[i].tags;
            updateQuestion.value = dto[i].question;
            updateAnswer.value = dto[i].answer;
            updateModal.style.display = "flex";
            break;
        }
    }
}

function updateModalSubmit(){
    if (updateQuestion.value.trim() === "") {
        alert("질문을 입력해주세요.");
        return;
    }
    if (updateAnswer.value.trim() === "") {
        alert("답변을 입력해주세요.");
        return;
    }

    updateModal.style.display = "none";

    // AJAX 수정 요청
    listHttpRequest = new XMLHttpRequest();
    listHttpRequest.onreadystatechange = postUpdateCard;

    function postUpdateCard(){
        if (listHttpRequest.readyState === XMLHttpRequest.DONE) {
            if (listHttpRequest.status === 200) {
                if (parseInt(listHttpRequest.response) == 1) {
                    alert("수정 성공");
                    // dto 및 테이블 내용 수정
                    const temp = document.querySelectorAll(".tableBody")[target];

                    // dto 수정
                    dto[target].cid = parseInt(updateCategory.dataset.value);
                    dto[target].tags = updateTags.value;
                    dto[target].question = updateQuestion.value;
                    dto[target].answer = updateAnswer.value;

                    // 테이블 수정
                    console.log(BigInt(updateCategory.dataset.value))
                    temp.children[1].innerText = categoryMap.get(parseInt(updateCategory.dataset.value));
                    temp.children[2].innerHTML = updateQuestion.value;
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
        '/card/cardUpdate');
    listHttpRequest.setRequestHeader('Content-type', 'application/json');
    let updateJSON = JSON.stringify({ id : dto[target].id,
        cid : updateCategory.dataset.value,
        question : updateQuestion.value,
        answer : updateAnswer.value,
        tags : updateTags.value});
    listHttpRequest.send(updateJSON);
}

function updateModalClose(){
    updateModal.style.display = "none";
}

// 삭제
const deleteModal = document.getElementById("modal-delete");
const deleteCaution = document.querySelector(".delete-caution");
function del(id){
    for (let i = 0; i < dto.length; i++) {
        if (dto[i].id === parseInt(id)) {
            target = i;
            break;
        }
    }
    deleteCaution.innerText = dto[target].id + "번을 정말로 삭제하시겠습니까?";
    deleteModal.style.display = "flex";
}

const cancel = document.querySelector('.cancel');
cancel.addEventListener('click', () => {
    deleteModal.style.display = "none";
});

const remove = document.querySelector('.remove');
remove.addEventListener('click', () => {
    // AJAX 삭제 요청
    if (target !== -1) {
        listHttpRequest = new XMLHttpRequest();
        listHttpRequest.onreadystatechange = postUpdateCard;

        function postUpdateCard(){
            if (listHttpRequest.readyState === XMLHttpRequest.DONE) {
                if (listHttpRequest.status === 200) {
                    if (parseInt(listHttpRequest.response) === 1) {
                        alert("삭제 성공");
                        window.location.reload();
                    }
                    else {
                        alert("삭제 실패");
                    }
                } else {
                    alert('Request Error!');
                }
                deleteModal.style.display = "none";
                target = -1;
            }
        }
        listHttpRequest.open('POST',
            '/card/cardDelete');
        listHttpRequest.setRequestHeader('Content-type', 'application/json');
        let deleteJSON = JSON.stringify({ id : dto[target].id,
            cid : dto[target].cid,
            question : dto[target].question,
            answer : dto[target].answer,
            tags : dto[target].tags});
        listHttpRequest.send(deleteJSON);
    }
});

window.onload = function(){
    // 0개 대응
    if (dto === null) {
        const main = document.querySelector(".container");
        main.innerHTML = "<br><br><br><br><br><br><br><br>검색 결과가 없습니다.<br><br>다른 검색어로 검색해주세요";
    }

    // 카테고리 변환
    const categoryCids = document.querySelectorAll(".category");
    for (let i = 0; i < categoryCids.length; i++) {
        categoryCids[i].innerText = categoryMap.get(parseInt(categoryCids[i].innerText));
    }

    // 카드 추가 이벤트
    function newCardHandler(event) {
        // 현재 리스트 조건에 맞는지 체크 후 추가
        let flag = false;

        if (criteria === "tag") {
            // 검색어 포함 태그 탐색
            if (event.detail.tags.includes(original)) {
                flag = true;
            }
        }
        else if (criteria === "question") {
            // 검색어 포함 질문 탐색
            if (event.detail.question.includes(original)) {
                flag = true;
            }
        }
        else if (criteria === "category") {
            if (event.detail.cid === parseInt(keyword)) {
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