// AJAX용 변수
var cManageHttpRequest;

// 삭제 처리
const cancel = document.querySelector('.cancel');
cancel.addEventListener('click', () => {
    dialog.close();
});

const remove = document.querySelector('.remove');
remove.addEventListener('click', () => {
    if (target != -1) {
        cManageHttpRequest = new XMLHttpRequest();
        cManageHttpRequest.onreadystatechange = postInsertCategory;

        function postInsertCategory(){
            if (cManageHttpRequest.readyState === XMLHttpRequest.DONE) {
                if (cManageHttpRequest.status === 200) {
                    if (cManageHttpRequest.response == 1) {
                        alert("삭제 성공");
                        // 해당 줄 삭제
                        const temp = document.querySelectorAll(".containerBody");
                        for (let i = 0; i < temp.length; i++) {
                            if (temp[i].children[0].children[0].innerText == dto[target].cname) {
                                temp[i].remove();
                                break;
                            }
                        }
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

        cManageHttpRequest.open('POST',
            '/card/categoryDelete'
            + "?cid=" + dto[target].cid
            + "&cname=" + dto[target].cname
            + "&cnt=" + dto[target].cnt);
        cManageHttpRequest.send()
    }
});

const dialog = document.querySelector('.dialog');
const checkCaution = document.querySelector('.checkCaution');
var target = -1;
function categoryDelete(index){
    if (dto[index].cnt != 0) {
        alert("해당 카테고리의 카드 수가 0개가 아니기에 삭제할 수 없습니다.");
    }
    else {
        checkCaution.textContent = dto[index].cname + " 카테고리를 정말로 삭제하시겠습니까?";
        target = index;
        if (typeof dialog.showModal === 'function') {
            dialog.showModal();
        } else {
            alert("현재 브라우저는 해당 기능을 지원하지 않습니다.")
        }
    }
}

// 카테고리 수정
function categoryUpdate(index){
    const original_name = document.querySelectorAll(".categoryName");
    const input_box = document.querySelectorAll(".newCategoryName");
    input_box[index].setAttribute("type", "text");
    input_box[index].value = original_name[index].innerText;
    original_name[index].innerText = "";
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
        createButton.style.display = "inline-block";
        check = false;
    }
}

function createCategory() {
    plusMark.innerText = "+";
    inputText.setAttribute("type", "hidden");
    createButton.style.display = "none";

    // AJAX 처리
    cManageHttpRequest = new XMLHttpRequest();
    cManageHttpRequest.onreadystatechange = postInsertCategory;

    function postInsertCategory(){
        if (cManageHttpRequest.readyState === XMLHttpRequest.DONE) {
            if (cManageHttpRequest.status === 200) {
                if (cManageHttpRequest.response != null) {
                    alert("추가 성공");
                    // 새로운 컨테이너 바디
                    let new_container = document.createElement("div");
                    new_container.setAttribute("class", "containerBody");

                    // div 추가
                    let new_div = document.createElement("div");
                    new_div.style.display = "inline-block";
                    new_div.setAttribute("class", "category");

                    let new_a = document.createElement("a");
                    new_a.text = inputText.value;
                    new_a.setAttribute("class", "categoryName");
                    new_div.appendChild(new_a);

                    let new_input = document.createElement("input");
                    new_input.setAttribute("type", "hidden");
                    new_input.setAttribute("class", "newCategoryName");
                    new_div.appendChild(new_input);

                    new_container.append(new_div);

                    // update 추가
                    let new_update = document.createElement("img");
                    new_update.setAttribute("class", "update");
                    new_update.setAttribute("height", "50px");
                    new_update.setAttribute("width", "auto");
                    new_update.setAttribute("src", "/img/edit_image.png");
                    new_update.setAttribute("onclick", "categoryUpdate('" + dto.length + "')")
                    new_container.append(new_update);

                    // delete 추가
                    let new_delete = document.createElement("img");
                    new_delete.setAttribute("class", "delete");
                    new_delete.setAttribute("height", "50px");
                    new_delete.setAttribute("width", "auto");
                    new_delete.setAttribute("src", "/img/delete_image.png");
                    new_delete.setAttribute("onclick", "categoryDelete('" + dto.length + "')")
                    new_container.append(new_delete);

                    // 새로운 줄 추가
                    const container_item = document.querySelector(".container-item");
                    const last_item = document.querySelector(".containerLast");
                    container_item.insertBefore(new_container, last_item);

                    // dtoList에 dto 추가
                    console.log(dto);

                }
                else {
                    alert("추가 실패");
                }
            } else {
                alert('Request Error!');
            }
            inputText.value = "";
            check = true;
        }
    }
    cManageHttpRequest.open('POST',
        '/card/categoryInsert'
        + "?cname=" + inputText.value
        + "&cnt=0");
    cManageHttpRequest.send()
}

window.onload = function(){
}