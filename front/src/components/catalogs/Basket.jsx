import Table from "../common/TableOrder";
export default function Basket(props) {
  return (
    <main className="flex-shrink-0" style={{ backgroundColor: "white" }}>
      <h1 className="my-5 ms-5 fs-1">
        <b>Корзина</b>
      </h1>
      <h2 className="my-5 ms-5 fs-3"></h2>
        <div className="ms-5 my-5">Список товаров</div>
        <Table product={props.product} setProduct={props.setProduct}></Table>
        <p> </p>
      <p> </p>
    </main>
  );
}
