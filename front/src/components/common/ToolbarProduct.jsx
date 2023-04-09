export default function ToolbarProduct(props) {
    function add() {
      props.onAdd();
    }
    return (
      <div className="btn-group mt-2" role="group">
        <button
          type="button"
          className={`btn btn-outline-dark text-center d-flex  justify-content-md-center mx-5 mb-3`}
          onClick={add}
        >
          Добавить
        </button>
      </div>
    );
  }
  