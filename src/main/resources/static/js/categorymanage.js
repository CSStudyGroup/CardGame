// 삭제 처리
let deleteTarget = -1;
const deleteModal = document.getElementById("modal-delete");
const deleteCaution = document.querySelector(".delete-caution");
const deleteCname = document.getElementById("modal-delete-cname");

function categoryDeleteCheck(name){
    deleteCaution.innerText = name + "을(를)\n정말로 삭제하시겠습니까?";
    deleteCname.innerText = name;
    deleteModal.style.display = "flex";
}

function deleteCancel(){
    deleteModal.style.display = "none";
}

function deleteConfirm(){
    categoryDelete(deleteCname.innerText);
    deleteModal.style.display = "none";
}

function categoryDelete(name){
    // 수정 도중 삭제 버튼을 눌렀을 경우
    if (updateCheck) {
        restoreUpdate(updateTarget);
    }

    // 추가 도중 삭제 버튼을 눌렀을 경우
    if (insertCheck) {
        restoreInsert(true);
    }

    // 탐색
    for (let i = 0; i < dto.length; i++) {
        if (dto[i].name === name) {
            deleteTarget = i;
            break;
        }
    }

    if (dto[deleteTarget].cardCount !== 0) {
        alert("해당 카테고리의 카드 수가 0개가 아니기에 삭제할 수 없습니다.");
    }
    else {
        // 해당 줄 삭제
        document.querySelectorAll(".container-body")[deleteTarget].remove();

        // dto 삭제
        dto.splice(deleteTarget, 1);

        // 버튼 등장
        changeSomething();
    }
    deleteTarget = -1;
}

// 카테고리 수정
let updateCheck = false;
let updateTarget = -1;
function categoryUpdate(name){

    // 추가 도중 수정 버튼을 눌렀을 경우
    if (insertCheck) {
        restoreInsert(true);
    }

    // 수정 도중 다른 수정 버튼을 눌렀을 경우
    if (updateCheck && dto[updateTarget].name !== name) {
        restoreUpdate(updateTarget);
    }

    if (updateCheck) {
        const input_box = document.querySelectorAll(".new-category-name");
        // 공백 체크
        if (input_box[updateTarget].value === "") {
            alert("공백은 이름으로 할 수 없습니다.");
            restoreUpdate(updateTarget);
            return;
        }

        // 중복체크
        for (let i = 0; i < dto.length; i++) {
            if (dto[i].name === input_box[updateTarget].value && i !== updateTarget) {
                alert("동일한 이름의 카테고리가 이미 존재합니다.");
                restoreUpdate(updateTarget);
                return;
            }
        }

        const updateButton = document.querySelectorAll(".update");
        const deleteButton = document.querySelectorAll(".delete");

        // 내용 수정 반영
        dto[updateTarget].name = input_box[updateTarget].value;
        updateButton[updateTarget].setAttribute("onclick",
            "categoryUpdate('" + input_box[updateTarget].value + "')")
        deleteButton[updateTarget].setAttribute("onclick",
            "categoryDelete('" + input_box[updateTarget].value + "')")

        // 버튼 등장
        changeSomething();

        // 원상 복귀
        restoreUpdate(updateTarget);
    }
    else {
        for (let i = 0; i < dto.length; i++) {
            if (dto[i].name === name) {
                updateTarget = i;
                break;
            }
        }

        const original_name = document.querySelectorAll(".category-name");
        const input_box = document.querySelectorAll(".new-category-name");
        const updateButton = document.querySelectorAll(".update");
        const deleteButton = document.querySelectorAll(".delete");

        updateButton[updateTarget].classList.add("target");
        deleteButton[updateTarget].classList.add("target");
        input_box[updateTarget].setAttribute("type", "text");
        input_box[updateTarget].value = dto[updateTarget].name;
        input_box[updateTarget].focus();
        original_name[updateTarget].innerText = "";
        updateCheck = true;
    }
}

// 카테고리 추가
let insertCheck = false;
const inputText = document.getElementById("create-category-name");
const plusMark = document.getElementById("plus-mark");
const createButton = document.getElementById("create-category-button");
const containerLast = document.querySelector(".container-last");

function categoryInsert() {
    // 수정 도중 추가 버튼을 눌렀을 경우
    if (updateCheck) {
        restoreUpdate(updateTarget);
    }

    if (!insertCheck) {
        containerLast.style.display = "block";
        plusMark.style.display = "none";
        inputText.setAttribute("type", "text");
        inputText.focus();
        createButton.style.display = "block";
        containerLast.classList.remove("clickable");
        containerLast.classList.add("hover");
        insertCheck = true;
    }
}

function createCategory() {
    if (inputText.value === "") {
        alert("공백은 이름으로 할 수 없습니다.");
        return;
    }
    // 중복체크
    for (let i = 0; i < dto.length; i++) {
        if (dto[i].name === inputText.value) {
            alert("동일한 이름의 카테고리가 이미 존재합니다.");
            return;
        }
    }

    // 새로운 컨테이너 바디
    let new_container = document.createElement("div");
    new_container.setAttribute("class", "container-body");

    // a추가
    let new_a = document.createElement("a");
    new_a.text = inputText.value + " (0)";
    new_a.setAttribute("class", "category-name");
    new_container.append(new_a);

    // input 추가
    let new_input = document.createElement("input");
    new_input.setAttribute("type", "hidden");
    new_input.setAttribute("class", "new-category-name");
    new_input.setAttribute("onkeyup", "enterEvent(event)");
    new_container.append(new_input);

    // delete 추가
    let new_delete = document.createElement("img");
    new_delete.setAttribute("class", "delete clickable");
    new_delete.setAttribute("height", "50px");
    new_delete.setAttribute("width", "auto");
    new_delete.setAttribute("src", "/img/delete_image.png");
    new_delete.setAttribute("onclick", "categoryDeleteCheck('" + inputText.value + "')")
    new_container.append(new_delete);

    // update 추가
    let new_update = document.createElement("img");
    new_update.setAttribute("class", "update clickable");
    new_update.setAttribute("height", "50px");
    new_update.setAttribute("width", "auto");
    new_update.setAttribute("src", "/img/edit_image.png");
    new_update.setAttribute("onclick", "categoryUpdate('" + inputText.value + "')")
    new_container.append(new_update);

    // 호버 이벤트 추가
    new_container.addEventListener("mouseenter", function (e) {
        e.target.classList.add("hover");
    });
    new_container.addEventListener("mouseleave", function (e) {
        if (!e.target.children[2].classList.contains("target")) {
            e.target.classList.remove("hover");
            e.target.classList.add("buttons-pop-down");
        }
    });

    // 새로운 줄 추가
    const container_item = document.querySelector(".container-item");
    const last_item = document.querySelector(".container-last");
    container_item.insertBefore(new_container, last_item);

    // dto 추가
    let new_dto = {};
    new_dto.id = null;
    new_dto.name = inputText.value;
    new_dto.cardCount = 0;
    dto.push(new_dto);

    // 원상 복구
    restoreInsert(false);

    // 버튼 등장
    changeSomething();

    // 동시 클릭 방지
    setTimeout(function () {
        insertCheck = false;
    }, 500);
}

// 추가 원상복구
function restoreInsert(check) {
    plusMark.style.display = "block";
    containerLast.style.display = "flex";
    inputText.setAttribute("type", "hidden");
    containerLast.classList.add("clickable");
    containerLast.classList.remove("hover");
    createButton.style.display = "none";
    inputText.value = "";
    if (check) {
        insertCheck = false;
    }
}

// 수정 원상 복구
function restoreUpdate(target) {
    const containerBody = document.querySelectorAll(".container-body");
    const original_name = document.querySelectorAll(".category-name");
    const input_box = document.querySelectorAll(".new-category-name");
    const updateButton = document.querySelectorAll(".update");
    const deleteButton = document.querySelectorAll(".delete");
    containerBody[target].classList.remove("hover");
    original_name[target].innerText = dto[target].name + " (" + dto[target].cardCount + ")";
    updateButton[target].classList.remove("target");
    deleteButton[target].classList.remove("target");
    input_box[target].setAttribute("type", "hidden");
    input_box[target].value = "";
    updateCheck = false;
    updateTarget = -1;
}

// change check
let changeCheck = true;
function changeSomething() {
    if (changeCheck) {
        const saveButton = document.querySelector(".save");
        saveButton.classList.add("save-pop-up");
        changeCheck = false;
    }
}

// 키보드 인식
function enterEvent (e) {
    if (e.key === "Enter") {
        e.preventDefault();
        if (updateCheck) {
            categoryUpdate(dto[updateTarget].name);
        }
        else if (insertCheck) {
            createCategory();
        }
    }
}

// 저장하지 않은 후 나갈 경우 체크
window.onbeforeunload = function() {
    if (!changeCheck) {
        return true;
    }
}

window.onload = function(){

    // 컨테이너 호버 이벤트
    const containerBodies = document.querySelectorAll(".container-body");
    for (let i = 0; i < containerBodies.length; i++) {
        containerBodies[i].addEventListener("mouseenter", function (e) {
            e.target.classList.add("hover");
        });
        containerBodies[i].addEventListener("mouseleave", function (e) {
            if (!e.target.children[2].classList.contains("target")) {
                e.target.classList.remove("hover");
                e.target.classList.add("buttons-pop-down");
            }
        });
    }

    // 외부 터치 인식
    window.addEventListener("click", function (e) {
        // insert 체크
        if (!e.target.classList.contains("insert")){
            if (insertCheck) {
                restoreInsert(true);
            }
        }

        // update 체크
        if (updateCheck) {
            const containerBody = document.querySelectorAll(".container-body");
            if (e.path[e.path.length - 7] !== containerBody[updateTarget]) {
                restoreUpdate(updateTarget);
            }
        }
    });

    // 카드 추가 이벤트
    function newCardHandler(event) {
        for (let i = 0; i < categoryDtoList.length; i++) {
            if (categoryDtoList[i].id === event.detail.cid) {
                categoryDtoList[i].cardCount += 1;
                // 존재할 경우 숫자 올리기
                for (let j = 0; j < dto.length; j++) {
                    if (dto[j].name === categoryDtoList[i].name) {
                        dto[j].cardCount += 1
                        const original_name = document.querySelectorAll(".category-name");
                        original_name[j].innerText = dto[j].name + " (" + dto[j].cardCount + ")";
                        break;
                    }
                }
                break;
            }
        }
    }
    document.addEventListener('newcard', newCardHandler);

    // 저장
    const changeModal = document.getElementById("modal-change");
    const save = document.getElementById("save");
    save.addEventListener('click', () => {
        // 추가 도중 저장 버튼을 눌렀을 경우
        if (insertCheck) {
            restoreInsert(true);
        }

        // 수정 도중 저장 버튼을 눌렀을 경우
        if (updateCheck) {
            restoreUpdate(updateTarget);
        }

        changeModal.style.display = "flex";
    });

    const cancel = document.querySelector('.cancel');
    cancel.addEventListener('click', () => {
        changeModal.style.display = "none";
    });

    const change = document.querySelector('.change');
    change.addEventListener('click', () => {
        // 쿼리 분류
        let insertCategory = [];
        let updateCategory = [];
        let deleteCategory = [];

        for (let i = 0; i < dto.length; i++) {
            // insert check
            if (dto[i].id == null) {
                insertCategory.push(dto[i]);
                continue;
            }

            // update check
            for (let j = 0; j < categoryDtoList.length; j++) {
                if (dto[i].id === categoryDtoList[j].id) {
                    if (dto[i].name !== categoryDtoList[j].name) {
                        updateCategory.push(dto[i]);
                        categoryDtoList[j].name = dto[i].name;
                    }
                    break
                }
            }
        }

        // delete check
        for (let i = categoryDtoList.length - 1; i >= 0; i--) {
            let flag = true;

            for (let j = 0; j < dto.length; j++) {
                if (categoryDtoList[i].id === dto[j].id) {
                    flag = false;
                    break
                }
            }

            if (flag) {
                if (categoryDtoList[i].cardCount === 0) {
                    deleteCategory.push(categoryDtoList[i]);
                    categoryDtoList.splice(i, 1);
                }
                else {
                    alert("카드가 있는 카테고리가 삭제되어 저장이 불가능합니다.\n새로고침을 시도합니다.");
                    window.location.reload();
                }
            }
        }

        // 변경사항 체크
        if (insertCategory.length === 0 && updateCategory.length === 0 && deleteCategory.length === 0) {
            alert("변경사항이 없습니다.");

            const saveButton = document.querySelector(".save");
            saveButton.classList.remove("save-pop-up");
            changeCheck = true;

            changeModal.style.display = "none";

            return;
        }

        // categoryURL 작성
        let categoryJSON = JSON.stringify({ insert : insertCategory, update : updateCategory, delete : deleteCategory});

        // AJAX 요청
        let categoryHttpRequest = new XMLHttpRequest();
        categoryHttpRequest.onreadystatechange = postCategories;

        function postCategories(){
            if (categoryHttpRequest.readyState === XMLHttpRequest.DONE) {
                if (categoryHttpRequest.status === 200) {
                    if (categoryHttpRequest.response.done) {
                        alert("저장 성공");
                        // categoryDtoList 수정
                        Array.prototype.push.apply(categoryDtoList, categoryHttpRequest.response.insertResult);

                        // dto 수정
                        for (let i = 0; i < dto.length; i++) {
                            if (dto[i].id == null) {
                                for (let j = 0; j < categoryHttpRequest.response.insertResult.length; j++) {
                                    if (dto[i].name === categoryHttpRequest.response.insertResult[j].name) {
                                        dto[i].id = categoryHttpRequest.response.insertResult[j].id;
                                        break
                                    }
                                }
                            }
                        }

                        // 버튼 숨김
                        const saveButton = document.querySelector(".save");
                        saveButton.classList.remove("save-pop-up");
                        changeCheck = true;

                        // 카테고리 수정 이벤트
                        const categoryChangedEvent = new CustomEvent('categoryChanged', {
                            detail: dto
                        });
                        document.dispatchEvent(categoryChangedEvent);
                    }
                    else {
                        alert("저장 실패");
                    }
                } else {
                    alert('Request Error!');
                }
                changeModal.style.display = "none";
            }
        }
        categoryHttpRequest.open('POST', encodeURI("/card/categoryChange"));
        categoryHttpRequest.responseType = "json";
        categoryHttpRequest.send(categoryJSON);
    });
}