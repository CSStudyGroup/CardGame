// 삭제 처리
var deleteTarget = -1;
function categoryDelete(cname){
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
    const original_name = document.querySelectorAll(".categoryName");
    const input_box = document.querySelectorAll(".newCategoryName");
    const updateButton = document.querySelectorAll(".update");
    const deleteButton = document.querySelectorAll(".delete");


    if (updateCheck) {
        // 중복체크
        for (let i = 0; i < dto.length; i++) {
            if (dto[i].cname == input_box[updateTarget].value && i != updateTarget) {
                alert("동일한 이름의 카테고리가 이미 존재합니다.");
                return;
            }
        }

        input_box[updateTarget].setAttribute("type", "hidden");
        original_name[updateTarget].innerText = input_box[updateTarget].value;
        dto[updateTarget].cname = input_box[updateTarget].value;
        updateButton[updateTarget].setAttribute("onclick",
            "categoryUpdate('" + input_box[updateTarget].value + "')")
        deleteButton[updateTarget].setAttribute("onclick",
            "categoryDelete('" + input_box[updateTarget].value + "')")

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
        input_box[updateTarget].value = original_name[updateTarget].innerText;
        original_name[updateTarget].innerText = "";
        updateCheck = true;
    }
}

// 카테고리 추가
var check = true;
const inputText = document.getElementById("newCategoryName");
const plusMark = document.getElementById("plusMark");
const createButton = document.getElementById("createCategory");

function categoryInsert() {
    if (check) {
        plusMark.innerText = "";
        inputText.setAttribute("type", "text");
        createButton.style.display = "block";
        check = false;
    }
}

function createCategory() {
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
    new_a.text = inputText.value;
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
    new_dto.cid = -1;
    new_dto.cname = inputText.value;
    new_dto.cnt = 0;
    dto.push(new_dto);

    // 원상 복구
    plusMark.innerText = "+";
    inputText.setAttribute("type", "hidden");
    createButton.style.display = "none";
    inputText.value = "";

    // 동시 클릭 방지
    setTimeout(function () {
        check = true;
    }, 500);
}

window.onload = function(){
    // 저장
    const save = document.getElementById("save");
    save.addEventListener('click', () => {
        alert("save try");

        // AJAX 전송 처리
       var cManageHttpRequest;
    });
}