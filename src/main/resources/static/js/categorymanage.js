// AJAX용 변수
var cManageHttpRequest;

function categoryDelete(index){
    if (dto[index].cnt != 0) {
        alert("해당 카테고리의 카드 수가 0개가 아니기에 삭제할 수 없습니다.");
    }
    else {
        // AJAX 처리
        console.log(dto[index]);
        cManageHttpRequest = new XMLHttpRequest();
        cManageHttpRequest.onreadystatechange = postInsertCategory;

        function postInsertCategory(){
            if (cManageHttpRequest.readyState === XMLHttpRequest.DONE) {
                if (cManageHttpRequest.status === 200) {
                    if (cManageHttpRequest.response == 1) {
                        alert("삭제 성공");
                        // 해당 줄 삭제
                    }
                    else {
                        alert("삭제 실패");
                    }
                } else {
                    alert('Request Error!');
                }
            }
        }
        cManageHttpRequest.open('POST',
            '/card/categoryDelete'
            + "?cid=" + dto[index].cid
            + "&cname=" + dto[index].cname
            + "&cnt=" + dto[index].cnt);
        cManageHttpRequest.send()
    }
}

function categoryUpdate(index){
    const input_box = document.querySelectorAll(".newCategoryName");
    console.log(input_box);
    input_box[index].setAttribute("type", "text");
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
                if (cManageHttpRequest.response == 1) {
                    alert("추가 성공");
                    // 새로운 컨테이너 바디
                    let new_container = document.createElement("div");
                    new_container.setAttribute("class", "containerBody");

                    // div 추가
                    let new_div = document.createElement("div");
                    new_div.style.display = "inline-block";
                    new_div.setAttribute("class", "category");
                    new_div.innerText = inputText.value;
                    new_container.append(new_div);
                    // 구조변경에 따른 추가 수정 필요..

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