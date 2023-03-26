"use strict";

window.addEventListener('DOMContentLoaded', function () {
    const host = "http://localhost:8080";
    const table = document.getElementById("tbody");
    const form = document.getElementById("form");
    const productIdInpit = document.getElementById("productId");
    const productNameInput = document.getElementById("productName");
    const priceInput = document.getElementById("productPrice");
    const componentIdInput = document.getElementById("componentId");
    const componentCountInput = document.getElementById("componentCount");
    const buttonRemove = document.getElementById("btnRemove");
    const buttonUpdate = document.getElementById("btnUpdate");
    const getData = async function () {

        table.innerHTML = "";
        const response = await fetch(host + "/product");
        const data = await response.json();
        data.forEach(Product => {
            let temp = "<select>";
            Product.componentDTOList.forEach(Component => {
                temp += `<option>${Component.componentName + " " + Component.count}</option>>`
            })
            temp += "</select>"
            table.innerHTML +=
                `<tr>
                        <th scope="row">${Product.id}</th>
                        <td>${Product.productName}</td>
                        <td>${Product.price}</td>
                        <td>${temp}</td>
                    </tr>`;
        })
    }

    const create = async function () {
        const requestParams = {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            }
        };
        const response = await fetch(host + `/product?price=${priceInput.value}&name=${productNameInput.value}&count=${componentCountInput.value}&comp=${componentIdInput.value}`, requestParams);
        return await response.json();
    }

    const remove = async function (){
        console.info('Try to remove item');
        if (productIdInpit.value !== 0) {
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
        const response = await fetch(host + `/product/` + productIdInpit.value, requestParams);
        return await response.json();
    }

    const update = async function (){
        console.info('Try to update item');
        if (productIdInpit.value === 0 || productNameInput.value == null || priceInput.value === 0 || componentIdInput.value === 0 || componentCountInput.value === 0) {
            return;
        }
        const requestParams = {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
            }
        };
        const response = await fetch(host + `/product/${productIdInpit.value}?price=${priceInput.value}&name=${productNameInput.value}&count=${componentCountInput.value}&comp=${componentIdInput.value}`, requestParams);
        return await response.json();
    }

    buttonRemove.addEventListener('click', function (event){
        event.preventDefault();
        remove().then((result) => {
            getData()
            productIdInpit.value = "";
        });
    });

    buttonUpdate.addEventListener('click', function (event){
        event.preventDefault();
        update().then((result) => {
            getData()
            componentIdInput.value = "";
            priceInput.value = "";
        });
    });

    form.addEventListener("submit", function (event) {
        event.preventDefault();
        create().then((result) => {
            getData();
            priceInput.value = "";
            productNameInput.value = "";
            alert(`Component[id=${result.id}, price=${result.price}, componentName=${result.productName}]`);
        });
    });
    getData();
});