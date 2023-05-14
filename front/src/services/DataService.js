import axios from 'axios';

export default class DataService {
    static dataUrlPrefix = 'http://localhost:8080';
    
    static async readAll(url, transformer) {
        const response = await fetch(this.dataUrlPrefix + url, {headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + localStorage.getItem("token")
        }});
        const data = await response.json();
        return data.map(item => transformer(item));
    }

    static async readUsersPage(dataUrlPrefix, url, page) {
        const response = await axios.get(dataUrlPrefix + url + `?page=${page}`,{
            headers:{
                "Authorization": "Bearer " + localStorage.getItem("token")
            }
        });
        return response.data;
    }
    static async readUser(dataUrlPrefix, url, login){
        const response = await axios.get(dataUrlPrefix + url + `/${login}`);
        return response.data;
    }
    
    static async read(url, transformer) {
        const response = await axios.get(this.dataUrlPrefix + url,{headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + localStorage.getItem("token")
        }});
        return transformer(response.data);
    }

    static async create(url, data) {
        const requestParams = {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(data),
        };
        const response = await fetch(this.dataUrlPrefix + url, requestParams);
    }

    static async update(url, data) {
        const requestParams = {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + localStorage.getItem("token")
            },
            body: JSON.stringify(data),
        };
        const response = await fetch(this.dataUrlPrefix + url, requestParams);
        return true;
    }

    static async delete(url) {
        const response = await axios.delete(this.dataUrlPrefix + url,{headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + localStorage.getItem("token")
        }});
        return response.data.id;
    }
    
    static async readUser(url, data) {
        const response = await axios.get(this.dataUrlPrefix + url + `/${data}`);
        return response.data;
    }
    static async readAllOrders(url, transformer) {
        const response = await fetch(this.dataUrlPrefix + url, {headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + localStorage.getItem("token")
        }});
        const data = await response.json();
        return data.map(item => transformer(item));
    }
}