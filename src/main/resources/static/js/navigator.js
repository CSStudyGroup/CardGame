// 내비게이션바 관련
const navbar = document.querySelector(".navbar");
const narrowNav = document.querySelector(".navbar-narrow");
const narrowNavOverlay = document.querySelector(".navbar-narrow-overlay");
const navbarSearchContainer = document.querySelector(".navbar-search-container");
const navbarSearch = document.querySelector(".navbar-search");
const navbarSearchShowButton = document.querySelector(".navbar-search-show");
function narrowNavbarShow() {
    narrowNavOverlay.style.display = "flex";
    narrowNav.classList.add('navbar-narrow-appear');
}

function narrowNavbarHide() {
    narrowNavOverlay.style.display = 'none';
    narrowNav.classList.remove('navbar-narrow-appear');
}

// 카드 추가를 위한 element
const insertModal = document.querySelector("#modal-insert");
const insertCategory = document.querySelector("#modal-insert-content-category-select");
const insertTags = document.querySelector("#modal-insert-content-tags");
const insertQuestion = document.querySelector("#modal-insert-content-question");
const insertAnswer = document.querySelector("#modal-insert-content-answer");

insertCategory.addEventListener('click', (e) => {
    e.stopPropagation();
})

insertModal.addEventListener('click', () => {
    insertCategory.classList.remove('sb-option-open');
});

function insert() {
    insertQuestion.value = "";
    insertAnswer.value = "";
    insertTags.value = "";
    insertModal.style.display = "flex";
}

function insertModalSubmit(){
    // 필수내용 작성 확인
    if (insertCategory.dataset.value === 'none') {
        alert('카테고리를 선택해주세요.');
        return;
    }
    if (insertQuestion.value.trim() === '') {
        alert('질문을 입력해주세요.');
        return;
    }
    if (insertAnswer.value.trim() === '') {
        alert('답변을 입력해주세요.');
        return;
    }

    insertModal.style.display = 'none';
    narrowNav.style.display = "block";

    // AJAX 추가 요청
    let cardHttpRequest = new XMLHttpRequest();
    cardHttpRequest.onreadystatechange = postInsertCard;
    function postInsertCard(){
        if (cardHttpRequest.readyState === XMLHttpRequest.DONE) {
            if (cardHttpRequest.status === 200) {
                let addedCard = cardHttpRequest.response;
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
    cardHttpRequest.open('POST', '/card/cardInsert')
    cardHttpRequest.setRequestHeader('Content-type', 'application/json');
    cardHttpRequest.send(JSON.stringify({
        cid: insertCategory.dataset.value,
        question: insertQuestion.value,
        answer: insertAnswer.value,
        tags: insertTags.value
    }));
}
function insertModalClose(){
    insertModal.style.display = 'none';
}

// 인터뷰 링크
const interviewModal = document.querySelector("#modal-interview");
const interviewForm = document.querySelector(".form-interview");
const checkList = document.querySelectorAll(".interview-category-checkbox");

function interview() {
    for(let i=0; i<checkList.length; i++) {
        checkList[i].checked = false;
    }
    interviewModal.style.display = "flex";
}

function interviewSubmit() {
    let check = 0;
    let count = 0;
    for(let i=0; i<checkList.length; i++) {
        if (checkList[i].checked) {
            check = true;
            count += categoryDtoList[i].cardCount;
        }
    }
    if (!check) {
        alert("카테고리를 하나 이상 선택해주세요.");
    }
    else if (count === 0) {
        alert("선택하신 카테고리에 해당하는 질문이 없습니다.");
    }
    else {
        interviewForm.submit();
    }
}
function interviewCancel() {
    interviewModal.style.display = 'none';
}

// 검색
const navbarSearchCriteria = document.querySelector("#navbar-search-criteria");
const navbarSearchKeyword = document.querySelector("#navbar-search-keyword");
const searchForm = document.querySelector("#navbar-search-form");
const searchCriteria = searchForm.querySelector('.search-criteria');
const searchKeyword = searchForm.querySelector('.search-keyword');
const searchOriginal = searchForm.querySelector('.search-original');

// 검색 결과 표시 페이지일 경우(list) 키워드 표시
if (window.location.pathname === "/card/list") {
    let criteria_text = '카테고리';
    if (criteria === 'tag') {
        criteria_text = '태그';
    }
    else if (criteria === 'question') {
        criteria_text = '질문';
    }
    selectOption('navbar-search-criteria', criteria_text, criteria);
    navbarSearchKeyword.value = original;
}

function search() {
    let kw = navbarSearchKeyword.value;
    searchCriteria.value = navbarSearchCriteria.dataset.value;
    if (searchCriteria.value == 'category') {
        let cid = -1;
        for (let i=0; i<categoryDtoList.length; i++) {
            if (categoryDtoList[i].name == kw) {
                cid = categoryDtoList[i].id;
                break;
            }
        }
        kw = cid;
    }
    searchKeyword.value = kw;
    searchOriginal.value = navbarSearchKeyword.value;
    searchForm.submit();
}

function searchEnterKey(e) {
    if (e.keyCode === 13) {
        search();
    }
}

// 드롭다운 검색창
function navbarSearchShow(e) {
    navbarSearchKeyword.value = '';
    navbarSearchContainer.classList.add('navbar-search-open');
    navbarSearchShowButton.style.visibility = "hidden";
    e.stopPropagation();
}

function navbarSearchHide() {
    navbarSearchContainer.classList.remove('navbar-search-open');
    navbarSearchShowButton.style.visibility = "visible";
    navbarSearchCriteria.classList.remove('sb-option-open');
}

navbarSearch.addEventListener('click', (e) => {
    e.stopPropagation();
})

document.addEventListener("click", () => {
    navbarSearchHide();
});

/* 사이트맵 드롭다운  */
const navbarSitemapDropdown = document.querySelector('.navbar-dropdown-sitemap');
const navbarSitemapDropdownOverlay = document.querySelector('.navbar-dropdown-overlay');
const sitemapList = document.querySelector('.sitemap-list');
const sitemapListItems = document.querySelectorAll('.sitemap-list-item');
const sitemapPreButton = document.querySelector('#btn-sitemap-pre');
const sitemapNxtButton = document.querySelector('#btn-sitemap-nxt');
let navbarSitemapDropdownTimer;
let selectedIndex = {
    _value: 0,
    _minBound: 0,
    _maxBound: sitemapListItems.length - 1,
    get value() {
        return this._value;
    },
    set value(v) {
        if (v < this._minBound) {
            this._value = this._minBound;
        }
        else if (v > this._maxBound) {
            this._value = this._maxBound;
        }
        this._value = v;
        setTransition('transform 0.3s linear');
        setTranslate(this._value);
        if (this._value === this._minBound) {
            sitemapPreButton.style.visibility = 'hidden';
        }
        else {
            sitemapPreButton.style.visibility = 'visible';
        }
        if (this._value === this._maxBound) {
            sitemapNxtButton.style.visibility = 'hidden';
        }
        else {
            sitemapNxtButton.style.visibility = 'visible';
        }
    }
};

function setTransition(value) {
    sitemapList.style.transition = value;
}

function setTranslate(index) {
    sitemapList.style.transform = `translate(-${(index) * 260}px, 0)`;
}

function showNavbarSitemapDropdown() {
    selectedIndex.value = 0;
    navbarSitemapDropdownOverlay.style.display='block';
    navbarSitemapDropdown.classList.add('navbar-dropdown-open');
    if (navbarSitemapDropdownTimer) {
        navbarSitemapDropdownTimer = clearTimeout(navbarSitemapDropdownTimer);
    }
}

function hideNavbarSitemapDropdown() {
    navbarSitemapDropdownTimer = setTimeout(function() {
        navbarSitemapDropdownOverlay.style.display='none';
        navbarSitemapDropdown.classList.remove('navbar-dropdown-open');
    }, 1);
}

/* slick slider 구현 */
function preItem() {
    selectedIndex.value -= 1;
}

function nxtItem() {
    selectedIndex.value += 1;
}


/* resize observer */
function resize(entries) {
    if (entries[0].contentRect.width > 600) {
        navbarSearchHide();
    }

    if (entries[0].contentRect.width > 1000) {
        narrowNavbarHide();
    }
}
const resizeObserver = new ResizeObserver(resize);
resizeObserver.observe(navbar);
