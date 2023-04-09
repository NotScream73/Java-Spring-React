import styles from './Toolbar.module.css';

export default function Toolbar(props) {
    function add() {
        props.onAdd();
    }

    function edit() {
        props.onEdit();
    }

    function remove() {
        props.onRemove();
    }

    return (
        <div className="btn-group mt-2" role="group">
            <button type="button" className={`btn btn-outline-dark text-center d-flex  justify-content-md-center mx-2 mb-3`} onClick={add}>
                Добавить
            </button>
            <button type="button" className={`btn btn-outline-dark text-center d-flex  justify-content-md-center mx-2 mb-3`} onClick={edit} >
                Изменить
            </button >
            <button type="button" className={`btn btn-outline-dark text-center d-flex  justify-content-md-center mx-2 mb-3`} onClick={remove}>
                Удалить
            </button >
        </div >
    );
}