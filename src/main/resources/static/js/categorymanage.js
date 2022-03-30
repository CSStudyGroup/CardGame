// 삭제 처리
var deleteTarget = -1;
function categoryDelete(cname){
    // 수정 도중 삭제 버튼을 눌렀을 경우
    if (updateCheck) {
        // 원상 복구
        const original_name = document.querySelectorAll(".categoryName");
        const input_box = document.querySelectorAll(".newCategoryName");
        original_name[updateTarget].innerText = dto[updateTarget].cname + " (" + dto[updateTarget].cnt + ")";
        input_box[updateTarget].setAttribute("type", "hidden");
        input_box[updateTarget].value = "";
        updateCheck = false;
        updateTarget = -1;
    }

    // 추가 도중 삭제 버튼을 눌렀을 경우
    if (insertCheck) {
        // 원상 복구
        plusMark.innerText = "+";
        inputText.setAttribute("type", "hidden");
        containerLast.classList.add("clickable");
        createButton.style.display = "none";
        inputText.value = "";
        insertCheck = false;
    }

    // 탐색
    for (let i = 0; i < dto.length; i++) {
        if (dto[i].cname == cname) {
            deleteTarget = i;
            break;
        }
    }

    if (dto[deleteTarget].cnt != 0) {
        alert("해당 카테고리의 카드 수가 0개가 아니기에 삭제할 수 없습니다.");
    }
    else {
        // 해당 줄 삭제
        document.querySelectorAll(".containerBody")[deleteTarget].remove();

        // dto 삭제
        dto.splice(deleteTarget, 1);
    }
    deleteTarget = -1;
}

// 카테고리 수정
var updateCheck = false;
var updateTarget = -1;
function categoryUpdate(cname){
    // 추가 도중 수정 버튼을 눌렀을 경우
    if (insertCheck) {
        // 원상 복구
        plusMark.innerText = "+";
        inputText.setAttribute("type", "hidden");
        containerLast.classList.add("clickable");
        createButton.style.display = "none";
        inputText.value = "";
        insertCheck = false;
    }

    // 수정 도중 다른 수정 버튼을 눌렀을 경우
    if (updateCheck && dto[updateTarget].cname != cname) {
        // 원상 복구
        const original_name = document.querySelectorAll(".categoryName");
        const input_box = document.querySelectorAll(".newCategoryName");
        original_name[updateTarget].innerText = dto[updateTarget].cname + " (" + dto[updateTarget].cnt + ")";
        input_box[updateTarget].setAttribute("type", "hidden");
        input_box[updateTarget].value = "";
        updateCheck = false;
        updateTarget = -1;
    }

    const original_name = document.querySelectorAll(".categoryName");
    const input_box = document.querySelectorAll(".newCategoryName");
    const updateButton = document.querySelectorAll(".update");
    const deleteButton = document.querySelectorAll(".delete");

    if (updateCheck) {
        // 공백 체크
        if (input_box[updateTarget].value == "") {
            alert("공백은 이름으로 할 수 없습니다.");
            input_box[updateTarget].setAttribute("type", "hidden");
            input_box[updateTarget].value = "";
            original_name[updateTarget].innerText = dto[updateTarget].cname + " (" + dto[updateTarget].cnt + ")";
            updateCheck = false;
            updateTarget = -1;
            return;
        }

        // 중복체크
        for (let i = 0; i < dto.length; i++) {
            if (dto[i].cname == input_box[updateTarget].value && i != updateTarget) {
                alert("동일한 이름의 카테고리가 이미 존재합니다.");
                input_box[updateTarget].setAttribute("type", "hidden");
                input_box[updateTarget].value = "";
                original_name[updateTarget].innerText = dto[updateTarget].cname + " (" + dto[updateTarget].cnt + ")";
                updateCheck = false;
                updateTarget = -1;
                return;
            }
        }

        // 내용 수정 반영
        original_name[updateTarget].innerText = input_box[updateTarget].value + " (" + dto[updateTarget].cnt + ")";
        dto[updateTarget].cname = input_box[updateTarget].value;
        updateButton[updateTarget].setAttribute("onclick",
            "categoryUpdate('" + input_box[updateTarget].value + "')")
        deleteButton[updateTarget].setAttribute("onclick",
            "categoryDelete('" + input_box[updateTarget].value + "')")

        // 원상 복귀
        input_box[updateTarget].setAttribute("type", "hidden");
        input_box[updateTarget].value = "";
        updateCheck = false;
        updateTarget = -1;
    }
    else {
        for (let i = 0; i < dto.length; i++) {
            if (dto[i].cname == cname) {
                updateTarget = i;
                break;
            }
        }
        input_box[updateTarget].setAttribute("type", "text");
        input_box[updateTarget].value = dto[updateTarget].cname;
        original_name[updateTarget].innerText = "";
        updateCheck = true;
    }
}

// 카테고리 추가
var insertCheck = false;
const inputText = document.getElementById("newCategoryName");
const plusMark = document.getElementById("plusMark");
const createButton = document.getElementById("createCategory");
const containerLast = document.querySelector(".containerLast");

function categoryInsert() {
    // 수정 도중 추가 버튼을 눌렀을 경우
    if (updateCheck) {
        // 원상 복구
        const original_name = document.querySelectorAll(".categoryName");
        const input_box = document.querySelectorAll(".newCategoryName");
        original_name[updateTarget].innerText = dto[updateTarget].cname + " (" + dto[updateTarget].cnt + ")";
        input_box[updateTarget].setAttribute("type", "hidden");
        input_box[updateTarget].value = "";
        updateCheck = false;
        updateTarget = -1;
    }

    if (!insertCheck) {
        plusMark.innerText = "";
        inputText.setAttribute("type", "text");
        createButton.style.display = "block";
        containerLast.classList.remove("clickable");
        insertCheck = true;
    }
}

function createCategory() {
    if (inputText.value == "") {
        alert("공백은 이름으로 할 수 없습니다.");
        return;
    }
    // 중복체크
    for (let i = 0; i < dto.length; i++) {
        if (dto[i].cname == inputText.value) {
            alert("동일한 이름의 카테고리가 이미 존재합니다.");
            return;
        }
    }

    // 새로운 컨테이너 바디
    let new_container = document.createElement("div");
    new_container.setAttribute("class", "containerBody");

    // a추가
    let new_a = document.createElement("a");
    new_a.text = inputText.value + " (0)";
    new_a.setAttribute("class", "categoryName");
    new_container.append(new_a);

    // input 추가
    let new_input = document.createElement("input");
    new_input.setAttribute("type", "hidden");
    new_input.setAttribute("class", "newCategoryName");
    new_container.append(new_input);

    // delete 추가
    let new_delete = document.createElement("img");
    new_delete.setAttribute("class", "delete clickable");
    new_delete.setAttribute("height", "50px");
    new_delete.setAttribute("width", "auto");
    new_delete.setAttribute("src", "/img/delete_image.png");
    new_delete.setAttribute("onclick", "categoryDelete('" + inputText.value + "')")
    new_container.append(new_delete);

    // update 추가
    let new_update = document.createElement("img");
    new_update.setAttribute("class", "update clickable");
    new_update.setAttribute("height", "50px");
    new_update.setAttribute("width", "auto");
    new_update.setAttribute("src", "/img/edit_image.png");
    new_update.setAttribute("onclick", "categoryUpdate('" + inputText.value + "')")
    new_container.append(new_update);

    // 새로운 줄 추가
    const container_item = document.querySelector(".container-item");
    const last_item = document.querySelector(".containerLast");
    container_item.insertBefore(new_container, last_item);

    // dto 추가
    var new_dto = new Object();
    new_dto.cid = null;
    new_dto.cname = inputText.value;
    new_dto.cnt = 0;
    dto.push(new_dto);

    // 원상 복구
    plusMark.innerText = "+";
    inputText.setAttribute("type", "hidden");
    containerLast.classList.add("clickable");
    createButton.style.display = "none";
    inputText.value = "";

    // 동시 클릭 방지
    setTimeout(function () {
        insertCheck = false;
    }, 500);
}

window.onload = function(){
    // 카드 추가 이벤트
    function newCardHandler(event) {
        for (let i = 0; i < categoryDtoList.length; i++) {
            if (categoryDtoList[i].cid == event.detail.cid) {
                categoryDtoList[i].cnt += 1;
                // 존재할 경우 숫자 올리기
                for (let j = 0; j < dto.length; j++) {
                    if (dto[j].cname == categoryDtoList[i].cname) {
                        dto[j].cnt += 1
                        const original_name = document.querySelectorAll(".categoryName");
                        original_name[j].innerText = dto[j].cname + " (" + dto[j].cnt + ")";
                        break;
                    }
                }
                break;
            }
        }
    }
    document.addEventListener('newcard', newCardHandler);

    // 저장
    const dialog = document.querySelector('.dialog');
    const save = document.getElementById("save");
    save.addEventListener('click', () => {
        // 추가 도중 저장 버튼을 눌렀을 경우
        if (insertCheck) {
            // 원상 복구
            plusMark.innerText = "+";
            inputText.setAttribute("type", "hidden");
            containerLast.classList.add("clickable");
            createButton.style.display = "none";
            inputText.value = "";
            insertCheck = false;
        }

        // 수정 도중 저장 버튼을 눌렀을 경우
        if (updateCheck) {
            // 원상 복구
            const original_name = document.querySelectorAll(".categoryName");
            const input_box = document.querySelectorAll(".newCategoryName");
            original_name[updateTarget].innerText = dto[updateTarget].cname + " (" + dto[updateTarget].cnt + ")";
            input_box[updateTarget].setAttribute("type", "hidden");
            input_box[updateTarget].value = "";
            updateCheck = false;
            updateTarget = -1;
        }

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

    const change = document.querySelector('.change');
    change.addEventListener('click', () => {
        // 쿼리 분류
        var insertCategory = [];
        var updateCategory = [];
        var deleteCategory = [];

        for (let i = 0; i < dto.length; i++) {
            // insert check
            if (dto[i].cid == null) {
                insertCategory.push(dto[i]);
                continue;
            }

            // update check
            for (let j = 0; j < categoryDtoList.length; j++) {
                if (dto[i].cid == categoryDtoList[j].cid) {
                    if (dto[i].cname != categoryDtoList[j].cname) {
                        updateCategory.push(dto[i]);
                        categoryDtoList[j].cname = dto[i].cname;
                    }
                    break
                }
            }
        }

        // delete check
        for (let i = categoryDtoList.length - 1; i >= 0; i--) {
            var flag = true;

            for (let j = 0; j < dto.length; j++) {
                if (categoryDtoList[i].cid == dto[j].cid) {
                    flag = false;
                    break
                }
            }

            if (flag) {
                deleteCategory.push(categoryDtoList[i]);
                categoryDtoList.splice(i, 1);
            }
        }

        // categoryURL 작성
        var categoryJSON = JSON.stringify({ insert : insertCategory, update : updateCategory, delete : deleteCategory});

        console.log(categoryJSON);

        // AJAX 요청
        var categoryHttpRequest = new XMLHttpRequest();
        categoryHttpRequest.onreadystatechange = postCategories;

        function postCategories(){
            if (categoryHttpRequest.readyState === XMLHttpRequest.DONE) {
                if (categoryHttpRequest.status === 200) {
                    if (categoryHttpRequest.response != null) {
                        alert("저장 성공");
                        // categoryDtoList 수정
                        Array.prototype.push.apply(categoryDtoList, categoryHttpRequest.response);

                        // dto 수정
                        for (let i = 0; i < dto.length; i++) {
                            if (dto[i].cid == null) {
                                for (let j = 0; j < categoryHttpRequest.response.length; j++) {
                                    if (dto[i].cname == categoryHttpRequest.response[j].cname) {
                                        dto[i].cid = categoryHttpRequest.response[j].cid;
                                        break
                                    }
                                }
                            }
                        }
                    }
                    else {
                        alert("저장 실패");
                    }
                } else {
                    alert('Request Error!');
                }
                dialog.close();
            }
        }
        categoryHttpRequest.open('POST', encodeURI("/card/categoryChange"));
        categoryHttpRequest.responseType = "json";
        categoryHttpRequest.send(categoryJSON);
    });
}