import { useState, useEffect } from 'react';
import Catalog from './Catalog';
import Component from '../../models/Component';
import DataService from '../../services/DataService';

export default function CatalogStudents(props) {
    const getAllUrl = '/component';
    const url = '/component/';
    const transformer = (data) => new Component(data);
    const catalogStudHeaders = [
        { name: 'componentName', label: 'Название компонента' },
        { name: 'price', label: 'Цена' }
    ];

    const [data, setData] = useState(new Component());

    function handleOnAdd() {
        setData(new Component());
    }

    function handleOnEdit(data) {
        setData(new Component(data));
    }

    function handleFormChange(event) {
        setData({ ...data, [event.target.id]: event.target.value })
    }

    return (
        <main className="flex-shrink-0" style={{ backgroundColor: "white" }}>
        <Catalog 
            headers={catalogStudHeaders} 
            getAllUrl={getAllUrl} 
            url={url}
            transformer={transformer}
            data={data}
            onAdd={handleOnAdd}
            onEdit={handleOnEdit}>
            <div className="mb-3">
                <label htmlFor="componentName" className="form-label">Название компонента</label>
                <input type="text" id="componentName" className="form-control" required
                    value={data.componentName} onChange={handleFormChange}/>
            </div>
            <div className="mb-3">
                <label htmlFor="price" className="form-label">Цена</label>
                <input type="text" id="price" className="form-control" required
                    value={data.price} onChange={handleFormChange}/>
            </div>
        </Catalog>
        </main>
    );
}