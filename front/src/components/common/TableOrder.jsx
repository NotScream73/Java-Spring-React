import { useEffect } from "react";
import { useState } from "react";
import DataService from "../../services/DataService";
import Order from "../../models/Order";

export default function TableOrder(props) {
  const [order, setOrder] = useState(new Order())
  const [cost, setCost] = useState(0);
  const [del, setDel] = useState(false);
  useEffect(() => {
    loadItems();
  }, []);
  useEffect(() => {
    loadItems();
    setDel(false);
  }, [del]);
  async function loadItems() {
    await summ(props.product.sort((a, b) => a.name > b.name ? 1 : -1));
    
    setOrder({...order,["productDTOList"]: props.product, ["price"]:cost});
  }
  async function summ(data) {
    let tem = 0;
    for (let i = 0; i < data.length; i++) {
      tem += data[i].count * Number(data[i].price);
    }
    setCost(tem);
    setOrder({...order,["price"]: tem});
  }
  async function deleteItem(item) {
    let currentProduct = props.product.filter((x) => x.id == item.id)[0];
    let temp = props.product.filter((x) => x.id != item.id);
    if (currentProduct.count - 1 == 0) {
      props.setProduct(temp);
      setDel(true);
      loadItems();
      return;
    } else {
      currentProduct.count--;
      temp.push(currentProduct);
      props.setProduct(temp);
      setDel(true);
      loadItems();
    }
  }
  async function acceptOrder(){
    await DataService.create("/order",{...order, ["price"]:cost, ["status"]: "1"} ).then(data => {
      props.setProduct([]);
      setCost(0);
    });
  }
  return (
    <div>
      <div style={{ maxWidth: "35%" }}>
        <table className="table">
          <thead>
            <tr>
              <th scope="col">#</th>
              <th scope="col">Позиция</th>
              <th scope="col"> </th>
              <th scope="col">Стоимость</th>
              <th scope="col"></th>
            </tr>
          </thead>
          <tbody className="table-group-divider">
            {props.product.map((item, index) => (
              <tr key={item.id}>
                <th scope="row">{index + 1}</th>
                <td colSpan="2">{item.name}</td>
                <td>
                  {item.count}x{item.price} руб
                </td>
                <td>
                  <button onClick={() => deleteItem(item)}>Удалить</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      <h2 className="ms-5 my-5">Итого: {cost} руб</h2>
      <button
        className="btn btn-success  ms-5 w-25"
        type="button"
        style={{ color: "black" }}
        onClick={acceptOrder}
      >
        Купить
      </button>
    </div>
  );
}
