"use strict";

window.addEventListener('DOMContentLoaded', function () {
    const host = "http://localhost:8080";
    const table = document.getElementById("tbody");
    const form = document.getElementById("form");
    const componentNameInput = document.getElementById("componentName");
    const priceInput = document.getElementById("price");
    const isEdit = false;
    const getData = async function () {
        table.innerHTML = "";
        const response = await fetch(host + "/component");
        const data = await response.json();
        data.forEach(Component => {
            table.innerHTML +=
                `<tr>
                        <th scope="row" id="componentId">${Component.id}</th>
                        <td>${Component.price}</td>
                        <td>${Component.componentName}</td>
                        <td><button onsubmit="edit">edit</button></td>
                    </tr>`;
        })
    }

    const create = async function (price, componentName) {
        const requestParams = {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            }
        };
        const response = await fetch(host + `/component?price=${price}&name=${componentName}`, requestParams);
        return await response.json();
    }

    const test = async function (testObject) {
        const requestParams = {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(testObject),
        };
        const response = await fetch(host + "/test", requestParams);
        if (response.status === 200) {
            const data = await response.json();
            alert(`TestDto=[id=${data.id}, name=${data.name}, data=${data.data}]`);
        }
        if (response.status === 400) {
            const data = await response.text();
            alert(data);
        }
    }
    function edit(){
        alert('хуй');
    }
    form.addEventListener("submit", function (event) {
        event.preventDefault();
        create(priceInput.value, componentNameInput.value).then((result) => {
            getData();
            priceInput.value = "";
            componentNameInput.value = "";
            alert(`Component[id=${result.id}, price=${result.price}, componentName=${result.componentName}]`);
        });
    });

    table.addEventListener("edit", function (event) {
        event.preventDefault();
        alert('хуй');
        create(priceInput.value, componentNameInput.value).then((result) => {
            getData();
            priceInput.value = "";
            componentNameInput.value = "";
            alert(`Component[id=${result.id}, price=${result.price}, componentName=${result.componentName}]`);
        });
    });
    getData();
});