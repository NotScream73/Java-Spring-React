import React from "react";

export default function Modal(props) {
    const formRef = React.createRef();

    function hide() {
        props.onHide();
    }

    function done(e) {
        e.preventDefault();
        if (formRef.current.checkValidity()) {
            props.onDone();
            hide();
        } else {
            formRef.current.reportValidity();
        }
        
    }

    return (
        <div className="modal fade show" tabIndex="-1" aria-hidden="true"
            style={{ display: props.visible ? 'block' : 'none' }}>
            <div className="modal-dialog">
                <div className="modal-content">
                    <div className="modal-header">
                        <h1 className="modal-title fs-5" id="exampleModalLabel">{props.header}</h1>
                        <button className="btn-close" type="button"  aria-label="Close"
                            onClick={hide}></button>
                    </div>
                    <div className="modal-body">
                        <form ref={formRef} onSubmit={done}>
                            {props.children}
                        </form>
                    </div>
                    <div className="modal-footer">
                        <button className="btn btn-secondary" type="button" onClick={hide}>Закрыть</button>
                        <button className="btn btn-primary" type="button" onClick={done}>
                            {props.confirm}
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
}