"use strict";

window.addEventListener('DOMContentLoaded', function () {
    const host = "http://localhost:8080";
    const table = document.getElementById("tbody");
    const form = document.getElementById("form");
    const componentNameInput = document.getElementById("componentName");
    const priceInput = document.getElementById("price");
    const componentIdInput = document.getElementById("componentId");
    const buttonRemove = document.getElementById("btnRemove");
    const buttonUpdate = document.getElementById("btnUpdate");
    const getData = async function () {
        table.innerHTML = "";
        const response = await fetch(host + "/component");
        const data = await response.json();
        data.forEach(Component => {
            table.innerHTML +=
                `<tr>
                        <th scope="row" id="componentId">${Component.id}</th>
                        <td>${Component.componentName}</td>
                        <td>${Component.price}</td>
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

    const remove = async function (){
        console.info('Try to remove item');
        if (itemId.value !== 0) {
            if (!confirm('Do you really want to remove this item?')) {
                console.info('Canceled');
                return;
            }
        }
        const requestParams = {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json",
            }
        };
        const response = await fetch(host + `/component/` + itemId.value, requestParams);
        return await response.json();
    }

    const update = async function (){
        console.info('Try to update item');
        if (componentIdInput.value === 0 || componentNameInput.value == null || priceInput.value === 0) {
            return;
        }
        const requestParams = {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
            }
        };
        const response = await fetch(host + `/component/${componentIdInput.value}?price=${priceInput.value}&name=${componentNameInput.value}`, requestParams);
        return await response.json();
    }

    buttonRemove.addEventListener('click', function (event){
        event.preventDefault();
        remove().then((result) => {
            getData()
            componentIdInput.value = "";
            priceInput.value = "";
            componentNameInput.value = "";
        });
    });

    buttonUpdate.addEventListener('click', function (event){
        event.preventDefault();
        update().then((result) => {
            getData()
            componentIdInput.value = "";
            priceInput.value = "";
            componentNameInput.value = "";
        });
    });

    form.addEventListener("submit", function (event) {
        event.preventDefault();
        create(priceInput.value, componentNameInput.value).then((result) => {
            getData();
            priceInput.value = "";
            componentNameInput.value = "";
            alert(`Component[id=${result.id}, price=${result.price}, componentName=${result.componentName}]`);
        });
    });
    getData();
});