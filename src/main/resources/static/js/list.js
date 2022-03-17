const viewmodal = document.querySelector(".viewmodal");
const id = document.getElementById("id");
const category = document.getElementById("category");
const tags = document.getElementById("tags");
const question = document.getElementById("question");
const answer = document.getElementById("answer");
var check = false;

function text(index) {
    if (!check) {
        id.innerText = dto[index].id;
        category.innerText = dto[index].category;
        tags.innerText = dto[index].tags;
        question.innerText = dto[index].question;
        answer.innerText = dto[index].answer;
        viewmodal.style.display = "block";
        check = true;
    }
}

function modalclose(){
    viewmodal.style.display = "none";
    check = false;
}

function update(test){
    if (!check) {
        alert("update : " + test);
    }
}

function del(test){
    if (!check) {
        alert("delete : " + test);
    }
}

window.onload = function(){

}