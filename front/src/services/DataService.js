import axios from 'axios';

export default class DataService {
    static dataUrlPrefix = 'http://localhost:8080';
    
    static async readAll(url, transformer) {
        const response = await fetch(this.dataUrlPrefix + url);
        const data = await response.json();
        return data.map(item => transformer(item));
    }

    static async read(url, transformer) {
        const response = await axios.get(this.dataUrlPrefix + url);
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
            },
            body: JSON.stringify(data),
        };
        const response = await fetch(this.dataUrlPrefix + url, requestParams);
        return true;
    }

    static async delete(url) {
        const response = await axios.delete(this.dataUrlPrefix + url);
        return response.data.id;
    }
}