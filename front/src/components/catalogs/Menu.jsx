import { useState, useEffect, Component } from "react";
import CatalogProduct from "./CatalogProduct";
import Product from "../../models/Product";
import Components from "../../models/Component";
import DataService from "../../services/DataService";
export default function Menu(props) {
  const url = "/product";
  const categoryUrl = "/component";
  const transformer = (data) => new Product(data);
  const catalogStudHeaders = [
    { name: "componentName", label: "Название компонента" },
    { name: "count", label: "Количество" },
  ];
  const [data, setData] = useState(new Product());
  const [component, setComponent] = useState([]);
  const [componentProduct, setComponentProduct] = useState([]);
  useEffect(() => {
    DataService.readAll(categoryUrl, (data) => new Components(data)).then(
      (data) => setComponent(data)
    );
  }, []);

  function handleOnAdd() {
    setData(new Product());
    setComponentProduct([]);
  }
  function handleOnEdit(data) {
    setData(new Product(data));
  }

  function handleFormChange(event) {
    setData({ ...data, [event.target.id]: event.target.value });
  }
  const [imageURL, setImageURL] = useState();
  const fileReader = new FileReader();
  fileReader.onloadend = () => {
    const tempval = fileReader.result;
    setImageURL(tempval);
    setData({ ...data, ["picture"]: tempval });
  };
  function handleOnChange(event) {
    event.preventDefault();
    const file = event.target.files[0];
    fileReader.readAsDataURL(file);
  }

  function handleUpdateComponents(value, bool) {
    if (bool) {
      let temp = data.componentDTOList.filter((x) => x.id != value.id);
      data.componentDTOList = [];
      for (let i = 0; i < temp.length; i++) {
        data.componentDTOList.push(temp[i]);
      }
      data.componentDTOList.push(value);
      setData(data);
    }
    setComponentProduct(
      componentProduct.map((obj) => {
        if (obj.id == value.id) {
          return { ...obj, count: value.count };
        } else {
          return obj;
        }
      })
    );
  }
  function setDataa(value) {
    setData({ ...data, ["image"]: value });
  }
  function setComponents() {
    data.componentDTOList = [];
    for (let i = 0; i < componentProduct.length; i++) {
      data.componentDTOList.push(componentProduct[i]);
    }
    setData(data);
  }
  function handleDeleteComponents(value) {
    setComponentProduct(componentProduct.filter((x) => x.id !== value));
  }
  function setprice(value){
    let count = 0;
      for (let i = 0; i < data.componentDTOList.length; i++) {
        count +=
        data.componentDTOList[i].price * data.componentDTOList[i].count;
      }
      data.price = count;
  }
  return (
    <main className="flex-shrink-0">
      <CatalogProduct
        url={url}
        transformer={transformer}
        data={data}
        onAdd={handleOnAdd}
        onEdit={handleOnEdit}
        handleFormChange={handleFormChange}
        handleOnChange={handleOnChange}
        component={component}
        catalogStudHeaders={catalogStudHeaders}
        componentProduct={componentProduct}
        setcomponentProduct={setComponentProduct}
        updateComponents={handleUpdateComponents}
        deleteComponents={handleDeleteComponents}
        setData={setDataa}
        set={setComponents}
        setprice={setprice}
        product={props.product}
        setProduct={props.setProduct}
      />
    </main>
  );
}
