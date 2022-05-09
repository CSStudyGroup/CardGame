const selectOption = function(selectBoxId, selectedOptionText, selectedOptionValue) {
    let sb = document.querySelector(`#${ selectBoxId }`);
    let sbOptionSelectedText = sb.querySelector('.sb-option-text');
    sb.dataset.value = selectedOptionValue;
    sbOptionSelectedText.textContent = selectedOptionText;
    sb.classList.remove('sb-option-open');
}

const loadingPage = document.querySelector('.loading-page');
window.addEventListener('load', () => {
    loadingPage.style.visibility = "hidden";
    loadingPage.style.opacity = "0";
})
