import { useState, useEffect } from "react";
import Table from "../common/Table";
import Modal from "../common/Modal";
import DataService from '../../services/DataService';

export default function CatalogHistory(props) {
    const [items, setItems] = useState([]);
    const [modalHeader, setModalHeader] = useState('');
    const [modalConfirm, setModalConfirm] = useState('');
    const [modalVisible, setModalVisible] = useState(false);
    const [isEdit, setEdit] = useState(false);
    

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

    function handleModalHide() {
        setModalVisible(false);
    }

    function handleModalDone() {
        saveItem();
    }

    return (
        <>
            <Table 
                headers={props.headers} 
                items={items}
                />
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