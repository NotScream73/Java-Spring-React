let calculateButton = document.getElementById("calculate");
let numberOneInput = document.getElementById("first");
let numberTwoInput = document.getElementById("second");
let resultInput = document.getElementById("res");

buttonPlus.onclick = function(event) {
    event.preventDefault();
    let num_1 = numberOneInput.value;
    let num_2 = numberTwoInput.value;
    fetch(`http://localhost:8080/sum?first=${num_1}&second=${num_2}`)
        .then(response => response.text())
        .then(res => {
                    const ru = new Intl.NumberFormat("ru").format(res);
                    resultInput.value = ru;
                    });
}
buttonMinus.onclick = function(event) {
   event.preventDefault();
   let num_1 = numberOneInput.value;
   let num_2 = numberTwoInput.value;
   fetch(`http://localhost:8080/minus?first=${num_1}&second=${num_2}`)
       .then(response => response.text())
       .then(res => {
                   const ru = new Intl.NumberFormat("ru").format(res);
                   resultInput.value = ru;
                   });
}
buttonMulti.onclick = function(event) {
   event.preventDefault();
   let num_1 = numberOneInput.value;
   let num_2 = numberTwoInput.value;

   fetch(`http://localhost:8080/multi?first=${num_1}&second=${num_2}`)
       .then(response => response.text())
       .then(res => {
       const ru = new Intl.NumberFormat("ru").format(res);
       resultInput.value = ru;
       });
}
buttonDiv.onclick = function(event) {
     event.preventDefault();
     let num_1 = numberOneInput.value;
     let num_2 = numberTwoInput.value;
     fetch(`http://localhost:8080/div?first=${num_1}&second=${num_2}`)
         .then(response => response.text())
         .then(res =>
         {
            const ru = new Intl.NumberFormat("ru").format(res);
            resultInput.value = ru;
         });
}