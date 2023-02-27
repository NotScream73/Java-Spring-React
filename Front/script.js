let calculateButton = document.getElementById("calculate");
let numberOneInput = document.getElementById("first");
let numberTwoInput = document.getElementById("second");
let resultInput = document.getElementById("res");
let typeInput = document.getElementById("type");

buttonPlus.onclick = function(event) {
    event.preventDefault();
    let num_1 = numberOneInput.value;
    let num_2 = numberTwoInput.value;
    let type = typeInput.value;
    fetch(`http://localhost:8080/sum?first=${num_1}&second=${num_2}&type=${type}`)
        .then(response => response.text())
        .then(res => {
                    resultInput.value = res;
                    });
}
buttonMinus.onclick = function(event) {
   event.preventDefault();
   let num_1 = numberOneInput.value;
   let num_2 = numberTwoInput.value;
   let type = typeInput.value;
   fetch(`http://localhost:8080/minus?first=${num_1}&second=${num_2}&type=${type}`)
       .then(response => response.text())
       .then(res => {
                   resultInput.value = res;
                   });
}
buttonMulti.onclick = function(event) {
   event.preventDefault();
   let num_1 = numberOneInput.value;
   let num_2 = numberTwoInput.value;
   let type = typeInput.value;
   fetch(`http://localhost:8080/multi?first=${num_1}&second=${num_2}&type=${type}`)
       .then(response => response.text())
       .then(res => {
       resultInput.value = res;
       });
}
buttonDiv.onclick = function(event) {
     event.preventDefault();
     let num_1 = numberOneInput.value;
     let num_2 = numberTwoInput.value;
     let type = typeInput.value;
     fetch(`http://localhost:8080/div?first=${num_1}&second=${num_2}&type=${type}`)
         .then(response => response.text())
         .then(res =>
         {
            resultInput.value = res;
         });
}