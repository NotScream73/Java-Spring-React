"use strict";

window.addEventListener('DOMContentLoaded', function () {
    const host = "http://localhost:8080";
    const table = document.getElementById("tbody");
    const form = document.getElementById("form");
    const orderIdInput = document.getElementById("orderId");
    const orderDate = document.getElementById("orderDate");
    const priceInput = document.getElementById("orderPrice");
    const productIdInput = document.getElementById("productId");
    const productCountInput = document.getElementById("productCount");
    const buttonRemove = document.getElementById("btnRemove");
    const buttonUpdate = document.getElementById("btnUpdate");

    const getData = async function () {

            table.innerHTML = "";
            const response = await fetch(host + "/order");
            const data = await response.json();
            data.forEach(Order => {
                let temp = "<select>";
                Order.productDTOList.forEach(Product => {
                    temp += `<option>${Product.productName + " " + Product.count}</option>>`
                })
                temp += "</select>"
                table.innerHTML +=
                    `<tr>
                            <th scope="row">${Order.id}</th>
                            <td>${Order.date}</td>
                            <td>${Order.price}</td>
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
            let temp = Date.now();
            let time = new Date(temp);
            let tru = time.toString();
            const response = await fetch(host + `/order?price=${priceInput.value}&date=${tru}&count=${productCountInput.value}&prod=${productIdInput.value}`, requestParams);
            return await response.json();
        }

        const remove = async function (){
            console.info('Try to remove item');
            if (orderIdInput.value !== 0) {
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
            const response = await fetch(host + `/order/` + orderIdInput.value, requestParams);
            return await response.json();
        }

        const update = async function (){
            console.info('Try to update item');
            if (orderIdInput.value === 0 || orderDate.value == null || orderPrice.value === 0 || productIdInput.value === 0 || productCountInput.value === 0) {
                return;
            }
            const requestParams = {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                }
            };
            let temp = Date.now();
            let time = new Date(temp);
            let tru = time.toString();
            const response = await fetch(host + `/order/${orderIdInput.value}?price=${priceInput.value}&date=${tru}&count=${productCountInput.value}&prod=${productIdInput.value}`, requestParams);
            return await response.json();
        }

        buttonRemove.addEventListener('click', function (event){
            event.preventDefault();
            remove().then((result) => {
                getData()
                orderIdInput.value = "";
            });
        });

        buttonUpdate.addEventListener('click', function (event){
            event.preventDefault();
            update().then((result) => {
                getData()
                orderIdInput.value = "";
                priceInput.value = "";
            });
        });

        form.addEventListener("submit", function (event) {
            event.preventDefault();
            create().then((result) => {
                getData();
                priceInput.value = "";
                orderIdInput.value = "";
                alert(`Component[id=${result.id}, price=${result.price}, componentName=${result.date}]`);
            });
        });
        getData();
});