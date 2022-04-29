// 카드 추가 이벤트 발생시 핸들러
const categoryList = document.querySelectorAll(".card-category")
function newCardHandler(event) {
    for(let i=0; i<categoryDtoList.length; i++) {
        if(categoryDtoList[i].name === event.detail.categoryName) {
            categoryDtoList[i].cardCount += 1;
            categoryList[i].children[1].textContent = parseInt(categoryList[i].children[1].textContent) + 1;
            break;
        }
    }
}
document.addEventListener('newcard', newCardHandler);