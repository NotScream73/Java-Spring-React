import DataService from "../../services/DataService";
export default function Card(props) {
  function edit(id) {
    props.onEdit(id);
  }

  function remove(id) {
    props.onRemove(id);
  }
  async function mess(id) {
    let currentProduct = props.product.filter((x) => x.id == id.id);
    if (currentProduct.length != 0) {
      let temp = props.product.filter((x) => x.id != id.id);
      currentProduct[0].count++;
      temp.push(currentProduct[0]);
      await props.setProduct(temp);
      return;
    } else {
      id.count++;
      props.product.push(id);
      props.setProduct(props.product);
    }
  }
  return (
    <div className="temp row row-cols-1 row-cols-md-3 g-4" id="tbl-items">
      {props.items.map((item) => (
        <div className="col" key={item.id}>
          <div className="card">
            <div
              className="container"
              style={{ width: "100%", height: "350px" }}
            >
              <img
                src={item["image"]}
                className="img-fluid rounded mx-auto d-block"
                style={{ width: "100%", height: "350px", objectFit: "contain" }}
                alt="Бугер"
              />
            </div>
            <div className="card-body">
              <h5 className="card-title text-center fs-1">{item["price"]}</h5>
              {localStorage.getItem("role") == "ADMIN" && (
                <>
                <a
                  href="#"
                  className="btn btn-outline-dark text-center d-flex justify-content-md-center mx-5"
                  onClick={(e) => remove(item.id, e)}
                >
                  Удалить
                </a>
                <a
                href="#"
                type="button"
                className="btn btn-outline-dark text-center d-flex justify-content-md-center mx-5"
                data-bs-toggle="modal"
                data-bs-target="#staticBackdrop"
                onClick={(e) => edit(item.id, e)}
              >
                Изменить
              </a>
                </>
              )}
              <a
                type="button"
                className="btn btn-outline-dark text-center d-flex justify-content-md-center mx-5"
                onClick={() => mess(item)}
              >
                в корзину
              </a>
            </div>
          </div>
        </div>
      ))}
    </div>
  );
}
