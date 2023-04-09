import { useState, useEffect, Component } from "react";
import Toolbar from "../common/ToolbarProduct";
import Card from "../common/Card";
import ModalProduct from "../common/ModalProduct";
import DataService from "../../services/DataService";
import Table from "../common/Table";
import ToolbarProduct from "../common/Toolbar";

export default function CatalogProduct(props) {
  const [items, setItems] = useState([]);
  const [modalHeader, setModalHeader] = useState("");
  const [modalConfirm, setModalConfirm] = useState("");
  const [modalVisible, setModalVisible] = useState(false);
  const [isEdit, setEdit] = useState(false);
  const [componentProduct, setComponentProduct] = useState({});
  useEffect(() => {
    loadItems();
  }, []);
  function loadItems() {
    DataService.readAll(props.url, props.transformer).then((data) =>{

      setItems(data);
    }
    );
    DataService.readAll(props.url, props.transformer).then((data) =>
      setItems(data)
    );
  }

  let selectedItems = [];
  async function saveItem() {
    if (!isEdit) {
      await props.set();
      DataService.create(props.url, props.data).then(() => loadItems());
    } else {
      await props.set();
      DataService.update(props.url + "/" + props.data.id, props.data).then(() =>
        loadItems()
      );
    }
  }

  function handleAdd() {
    setEdit(false);
    setModalHeader("Добавление элемента");
    setModalConfirm("Добавить");
    setModalVisible(true);
    props.onAdd();
  }

  function handleEdit(id) {
    if (selectedItems.length === 0) {
      return;
    }
    setComponentProduct(
      props.componentProduct.filter((x) => x.id == selectedItems[0])[0]
    );
  }

  function edit(editedId) {
    DataService.read(props.url+ "/" + editedId, props.transformer).then((data) => {
      for(let i = 0; i < data.componentDTOList.length; i++){
        props.componentProduct.push(data.componentDTOList[i]);
        props.setcomponentProduct(props.componentProduct)
      }
      setEdit(true);
      setModalHeader("Редактирование элемента");
      setModalConfirm("Сохранить");
      setModalVisible(true);
      props.onEdit(data);
    });
  }
  
  function handleRemove(id, e) {
    if (selectedItems.length === 0) {
      return;
    }
    if (confirm("Удалить выбранные элементы?")) {
      const promises = [];
      selectedItems.forEach((item) => {
        props.deleteComponents(item);
      });
      selectedItems.length = 0;
    }
  }
  function handleModalHide() {
    setModalVisible(false);
    props.setcomponentProduct([]);
  }

  function handleModalDone() {
    saveItem();
  }
  async function handleAddComponent() {
    if (
      props.componentProduct.filter((x) => x.id == componentProduct.id)
        .length != 0
    ) {
      await props.updateComponents(componentProduct, isEdit);
      setComponentProduct({});
      let count = 0;
      for (let i = 0; i < props.componentProduct.length; i++) {
        count +=
          props.componentProduct[i].price * props.componentProduct[i].count;
      }
      props.data.price = count;
      
      if(isEdit){
        await props.setprice(count);
        return; 
      }
      return;
    }
    props.componentProduct.push(componentProduct);
    await props.setcomponentProduct(props.componentProduct);
    setComponentProduct({});
    let count = 0;
    for (let i = 0; i < props.componentProduct.length; i++) {
      count +=
        props.componentProduct[i].price * props.componentProduct[i].count;
    }
    props.data.price = count;
  }
  function handleRemoveProduct(id) {
    if (confirm("Удалить выбранные элементы?")) {
      DataService.delete(props.url + "/" + id).then(() => {
        loadItems();
      });
    }
  }
  function handleFormChangeComponent(event) {
    if (event.target.id === "componentName") {
      setComponentProduct({
        ...componentProduct,
        ["id"]: event.target.value,
        ["componentName"]: props.component
          .filter((x) => x.id == event.target.value)
          .map((x) => x.componentName)[0],
        ["price"]: props.component
          .filter((x) => x.id == event.target.value)
          .map((x) => x.price)[0],
      });
      return;
    }
    setComponentProduct({
      ...componentProduct,
      [event.target.id]: Number(event.target.value),
    });
    return;
  }
  const [imageURL, setImageURL] = useState();
  const fileReader = new FileReader();
  fileReader.onloadend = () => {
    const tempval = fileReader.result;
    setImageURL(tempval);
    props.setData(tempval);
  };
  function handleOnChange(event) {
    event.preventDefault();
    const file = event.target.files[0];
    fileReader.readAsDataURL(file);
  }
  function handleTableClick(tableSelectedItems) {
    selectedItems = tableSelectedItems;
  }
  function handleTableDblClick(tableSelectedItem) {
    setComponentProduct(
      props.componentProduct.filter((x) => x.id == tableSelectedItem)[0]
    );
  }
  return (
    <>
      <Toolbar onAdd={handleAdd} />
      <Card items={items} onEdit={edit} onRemove={handleRemoveProduct} product={props.product} setProduct={props.setProduct}/>
      <ModalProduct
        header={modalHeader}
        confirm={modalConfirm}
        visible={modalVisible}
        onHide={handleModalHide}
        onDone={handleModalDone}
      >
        <div className="mb-3">
          <label htmlFor="name" className="form-label">
            Название продукта
          </label>
          <input
            type="text"
            id="name"
            className="form-control"
            required
            value={props.data.name}
            onChange={props.handleFormChange}
          />
        </div>
        <div className="mb-3">
          <label htmlFor="price" className="form-label">
            Цена
          </label>
          <input
            type="text"
            id="price"
            className="form-control"
            required
            value={props.data.price}
            onChange={props.handleFormChange}
          />
        </div>
        <div className="col-mb-3">
          <label className="form-label" htmlFor="picture">
            Изображение
          </label>
          <input
            className="form-control"
            id="picture"
            type="file"
            accept="image/jpeg, image/png, image/jpg"
            value=""
            onChange={handleOnChange}
          />
        </div>
        <div className="mb-3">
          <label htmlFor="groupId" className="form-label">
            Компонент
          </label>
          <select
            id="componentName"
            className="form-select"
            required
            value={componentProduct.id}
            onChange={handleFormChangeComponent}
          >
            <option disabled value="">
              Укажите группу
            </option>
            {props.component.map((group) => (
              <option key={group.id} value={group.id}>
                {group.componentName}
              </option>
            ))}
          </select>
          <label htmlFor="count" className="form-label">
            Количество
          </label>
          <input
            type="text"
            id="count"
            className="form-control"
            required
            value={componentProduct.count ?? 0}
            onChange={handleFormChangeComponent}
          />
        </div>
        <ToolbarProduct
          onAdd={handleAddComponent}
          onEdit={handleEdit}
          onRemove={handleRemove}
        />
        <Table
          headers={props.catalogStudHeaders}
          items={props.componentProduct}
          allItems={props.component}
          selectable={true}
          onClick={handleTableClick}
          onDblClick={handleTableDblClick}
        />
      </ModalProduct>
    </>
  );
}
