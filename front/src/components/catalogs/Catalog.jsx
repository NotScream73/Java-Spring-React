import { useState, useEffect } from "react";
import Toolbar from "../common/Toolbar";
import Table from "../common/Table";
import Modal from "../common/Modal";
import DataService from '../../services/DataService';

export default function Catalog(props) {
    const [items, setItems] = useState([]);
    const [modalHeader, setModalHeader] = useState('');
    const [modalConfirm, setModalConfirm] = useState('');
    const [modalVisible, setModalVisible] = useState(false);
    const [isEdit, setEdit] = useState(false);
    
    let selectedItems = [];

    useEffect(() => {
        loadItems();
    }, []);

    function loadItems() {
        DataService.readAll(props.getAllUrl, props.transformer)
            .then(data => setItems(data));
    }

    function saveItem() {
        if (!isEdit) {
            DataService.create(props.getAllUrl, props.data).then(() => loadItems());
        } else {
            DataService.update(props.url + props.data.id, props.data).then(() => loadItems());
        }
    }

    function handleAdd() {
        setEdit(false);
        setModalHeader('Добавление элемента');
        setModalConfirm('Добавить');
        setModalVisible(true);
        props.onAdd();
    }

    function handleEdit() {
        if (selectedItems.length === 0) {
            return;
        }
        edit(selectedItems[0]);
    }

    function edit(editedId) {
        DataService.read(props.url + editedId, props.transformer)
            .then(data => {
                setEdit(true);
                setModalHeader('Редактирование элемента');
                setModalConfirm('Сохранить');
                setModalVisible(true);
                props.onEdit(data);
            });
    }

    function handleRemove() {
        if (selectedItems.length === 0) {
            return;
        }
        if (confirm('Удалить выбранные элементы?')) {
            const promises = [];
            selectedItems.forEach(item => {
                promises.push(DataService.delete(props.url + item));
            });
            Promise.all(promises).then((results) => {
                selectedItems.length = 0;
                loadItems();
            });
        }
    }

    function handleTableClick(tableSelectedItems) {
        selectedItems = tableSelectedItems;
    }

    function handleTableDblClick(tableSelectedItem) {
        edit(tableSelectedItem);
    }

    function handleModalHide() {
        setModalVisible(false);
    }

    function handleModalDone() {
        saveItem();
    }

    return (
        <>
            <Toolbar 
                onAdd={handleAdd}
                onEdit={handleEdit}
                onRemove={handleRemove}/>
            <Table 
                headers={props.headers} 
                items={items}
                selectable={true}
                onClick={handleTableClick}
                onDblClick={handleTableDblClick}/>
            <Modal 
                header={modalHeader}
                confirm={modalConfirm}
                visible={modalVisible} 
                onHide={handleModalHide}
                onDone={handleModalDone}>
                    {props.children}
            </Modal>
        </>
    );
}