"use strict";

window.addEventListener('DOMContentLoaded', function () {
    const host = "http://localhost:8080";
    const table = document.getElementById("tbody");
    const form = document.getElementById("form");
    const lastNameInput = document.getElementById("componentName");
    const firstNameInput = document.getElementById("firstName");
    const testErrorBtn = document.getElementById("testError");
    const testNormalBtn = document.getElementById("testNormal");

    const getData = async function () {
        table.innerHTML = "";
        const response = await fetch(host + "/student");
        const data = await response.json();
        data.forEach(student => {
            table.innerHTML +=
                `<tr>
                        <th scope="row">${student.id}</th>
                        <td>${student.firstName}</td>
                        <td>${student.lastName}</td>
                    </tr>`;
        })
    }

    const create = async function (firstName, lastName) {
        const requestParams = {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            }
        };
        const response = await fetch(host + `/student?firstName=${firstName}&lastName=${lastName}`, requestParams);
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

    form.addEventListener("submit", function (event) {
        event.preventDefault();
        create(firstNameInput.value, lastNameInput.value).then((result) => {
            getData();
            firstNameInput.value = "";
            lastNameInput.value = "";
            alert(`Student[id=${result.id}, firstName=${result.firstName}, lastName=${result.lastName}]`);
        });
    });

    testErrorBtn.addEventListener("click", function () {
        test({});
    });

    testNormalBtn.addEventListener("click", function () {
        test({id: 10, name: "test"});
    });

    getData();
});