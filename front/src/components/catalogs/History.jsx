import { useState, useEffect } from 'react';
import Catalog from './CatalogHistory';
import Component from '../../models/Component';
import DataService from '../../services/DataService';
import Order from '../../models/Order';

export default function CatalogStudents(props) {
    const getAllUrl = '/order';
    const url = '/order/';
    const transformer = (data) => new Order(data);
    const catalogStudHeaders = [
        { name: 'date', label: 'Дата оформления' },
        { name: 'price', label: 'Общая стоимость' },
        { name: 'status', label: 'Статус' }
    ];

    const [data, setData] = useState(new Order());

    function handleOnAdd() {
        setData(new Order());
    }

    function handleOnEdit(data) {
        setData(new Order(data));
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