import { useState } from 'react';
import styles from './Table.module.css';

export default function Table(props) {
    const [tableUpdate, setTableUpdate] = useState(false);
    const [selectedItems, setSelectedItems] = useState([]);

    function isSelected(id) {
        if (!props.selectable) {
            return false;
        }
        return selectedItems.includes(id);
    }

    function click(id) {
        if (!props.selectable) {
            return;
        }
        if (isSelected(id)) {
            var index = selectedItems.indexOf(id);
            if (index !== -1) {
                selectedItems.splice(index, 1);
                setSelectedItems(selectedItems);
                setTableUpdate(!tableUpdate);
            }
        } else {
            selectedItems.push(id);
            setSelectedItems(selectedItems);
            setTableUpdate(!tableUpdate);
        }
        props.onClick(selectedItems);
    }

    function dblClick(id) {
        if (!props.selectable) {
            return;
        }
        props.onDblClick(id);
    }

    return (
        <table className={`table table-hover ${styles.table} ${props.selectable ? styles.selectable : ''}`}>
            <thead>
                <tr>
                    <th scope="col">#</th>
                    {
                        props.headers.map(header =>
                            <th key={header.name} scope="col">
                                {header.label}
                            </th>
                        )
                    }
                </tr>
            </thead>
            <tbody>
                {
                    props.items.map((item, index) =>
                        <tr key={item.id}
                            className={isSelected(item.id) ? styles.selected : ''}
                            onClick={(e) => click(item.id, e)} onDoubleClick={(e) => dblClick(item.id, e)}>
                            <th scope="row">{index + 1}</th>
                            {
                                props.headers.map(header =>
                                    <td key={item.id + header.name}>{item[header.name]}</td>
                                )
                            }
                        </tr>
                    )
                }
            </tbody >
        </table >
    );
}