import {NavLink, useNavigate} from "react-router-dom";
import {useEffect, useState} from "react";

export default function Header(props) {
    const [userRole, setUserRole] = useState("");
    const navigate = useNavigate();
    useEffect(() => {
        window.addEventListener("storage", () => {
            getUserRole();
        });
        getUserRole();
    }, []);

    const getUserRole = function () {
        const role = localStorage.getItem("role") || "NONE";
        setUserRole(role);
    };
    const handlelogout = function () {
        window.location.reload();
        navigate("/catalogs/login");
        localStorage.removeItem("role");
        localStorage.removeItem("user");
        localStorage.removeItem("token");
    }
    return (
        <nav className="navbar navbar-expand-lg">
            <div className="container-fluid">
                <NavLink className="navbar-brand" to={"/"}>
                    <h1>Очень вкусно и запятая</h1>
                </NavLink>
                <button
                    className="navbar-toggler"
                    type="button"
                    data-bs-toggle="collapse"
                    data-bs-target="#navbarSupportedContent"
                    aria-controls="navbarSupportedContent"
                    aria-expanded="false"
                    aria-label="Toggle navigation"
                >
                    <span className="navbar-toggler-icon"></span>
                </button>
                <div className="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul className="navbar-nav me-auto mb-2 mb-lg-0">
                        {props.links.map((route) => {
                            if (route.role == userRole || route.role == undefined) {
                                return (
                                    <li key={route.path} className="nav-item">
                                        <NavLink className="nav-link" to={route.path}>
                                            {route.label}
                                        </NavLink>
                                    </li>
                                );
                            }
                        })}
                    </ul>
                </div>
                <span className="col text-end">
            {localStorage.getItem("role") !== null &&
                <a className="nav-link" onClick={handlelogout}>
                    {"Выход(" + localStorage.getItem("user") + ")"}
                </a>
            }
          </span>
            </div>
        </nav>
    );
}
