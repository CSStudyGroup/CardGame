// 카드 추가를 위한 element
const insertModal = document.querySelector("#insertModal");
const insertModalOverlay = document.querySelector("#insertModalOverlay");
const insertCategory = document.querySelector("#insertCategory");
const insertTags = document.querySelector("#insertTags");
const insertQuestion = document.querySelector("#insertQuestion");
const insertAnswer = document.querySelector("#insertAnswer");

function insert() {
    insertCategory.value = "none";
    insertQuestion.value = "";
    insertAnswer.value = "";
    insertTags.value = "";
    insertModal.style.display = "block";
    insertModalOverlay.style.display = "flex";
}

function insertModalSubmit(){
    insertModal.style.display = "none";
    insertModalOverlay.style.display = "none";

    // AJAX 수정 요청
    let httpRequest = new XMLHttpRequest();
    httpRequest.onreadystatechange = postInsertCard;
    function postInsertCard(){
        if (httpRequest.readyState === XMLHttpRequest.DONE) {
            if (httpRequest.status === 200) {
                let addedCard = httpRequest.response;
                if (addedCard != null) {
                    alert("삽입 성공");
                    const newCardAddedEvent = new CustomEvent('newcard', {
                        detail: JSON.parse(addedCard)
                    });
                    document.dispatchEvent(newCardAddedEvent);
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
        + "?cid=" + insertCategory.value
        + "&question=" + insertQuestion.value
        + "&answer=" + insertAnswer.value
        + "&tags=" + insertTags.value );
    httpRequest.send();
}
function insertModalClose(){
    insertModal.style.display = "none";
    insertModalOverlay.style.display = "none";
}

// 인터뷰 링크
const interviewDialog = document.querySelector("#dialog-interview");

function interview() {
    if (typeof interviewDialog.showModal === 'function') {
        interviewDialog.showModal();
    } else {
        alert("현재 브라우저는 해당 기능을 지원하지 않습니다.")
    }
}

const interviewForm = document.querySelector("#interviewForm");
function interviewSubmit() {
    let check = 0;
    let count = 0;
    for(let i=0; i<interviewForm.childElementCount; i++) {
        if (interviewForm.children[i].firstElementChild.checked) {
            check = true;
            count += categoryDtoList[i].cnt;
        }
    }
    if (!check) {
        alert("카테고리를 하나 이상 선택해주세요.");
    }
    else if (count == 0) {
        alert("선택하신 카테고리에 해당하는 질문이 없습니다.");
    }
    else {
        interviewForm.submit();
    }
}

// 검색
const searchCriteria = document.querySelector("#searchCriteria");
const searchKeyword = document.querySelector("#searchKeyword");
const categoryKey = document.querySelector("#categoryKey");
const tagKey = document.querySelector("#tagKey");
const questionKey = document.querySelector("#questionKey");
const searchForm = document.querySelector("#searchForm");

function search() {
    let kw = searchKeyword.value;
    switch(searchCriteria.value) {
        case 'category':
            let cid = null;
            for(let i=0; i<categoryDtoList.length; i++) {
                if(categoryDtoList[i].cname == kw) {
                    cid = categoryDtoList[i].cid;
                    break;
                }
            }
            categoryKey.value = cid;
            break;
        case 'tag':
            tagKey.value = kw;
            break;
        case 'question':
            questionKey.value = kw;
            break;
    }

    searchForm.submit();
}

// 내비게이션바 크기변화시 본문 패딩 자동변화
const mainDiv = document.querySelector(".main");
const navbar = document.querySelector(".navbar");
const navbarMenu = navbar.querySelector(".navbar-menu");
const navbarToggler = navbar.querySelector(".navbar-toggler");
function resize(entries) {
    mainDiv.style.paddingTop = `${ entries[0].contentRect.height }px`;
    if (entries[0].contentRect.width < 1000) {
        navbarMenu.style.display = "none";
        navbarToggler.style.display = "inline-block";
    }
    else {
        navbarMenu.style.display = "flex";
        navbarToggler.style.display = "none";
    }
}
const resizeObserver = new ResizeObserver(resize);
resizeObserver.observe(navbar);