import "./App.css";
import {
  useRoutes,
  Outlet,
  BrowserRouter,
  Routes,
  Route,
} from "react-router-dom";
import Header from "./components/common/Header";
import PrivateRoute from "./components/common/PrivateRoute";
import Footer from "./components/common/Footer";
import CatalogStudents from "./components/catalogs/CatalogStudents";
import Menu from "./components/catalogs/Menu";
import Basket from "./components/catalogs/Basket";
import History from "./components/catalogs/History";
import Registration from "./components/catalogs/Registration";
import { useState } from "react";
import Login from "./components/catalogs/Login";
import Users from "./components/catalogs/Users";

function Router(props) {
  return useRoutes(props.rootRoute);
}

export default function App() {
  const [product, setProduct] = useState([]);
  const routes = [
    { index: true, element: <CatalogStudents /> },
    {
      path: "catalogs/menu",
      label: "Меню",
    },
    {
      path: "catalogs/component",
      label: "Компоненты",
      role: "ADMIN",
    },
    {
      path: "catalogs/basket",
      label: "Корзина",
    },
    { path: "catalogs/history", label: "История" },
    {
      path: "catalogs/users",
      label: "Пользователи",
      role: "ADMIN",
    },
    {
      path: "catalogs/registration",
      label: "Регистрация",
    },
    {
      path: "catalogs/login",
      label: "Вход в систему",
    },
  ];
  const links = routes.filter((route) => route.hasOwnProperty("label"));

  return (
    <BrowserRouter>
      <Header links={links} />
      <div className="content-div">
        <Routes>
          <Route element={<PrivateRoute role="USER" />}>
            <Route
              element={<Menu product={product} setProduct={setProduct} />}
              path="/catalogs/menu"
              exact
            />
            <Route
              element={<Menu product={product} setProduct={setProduct} />}
              path="*"
            />
            <Route
              element={<Basket product={product} setProduct={setProduct} />}
              path="/catalogs/basket"
            />
            <Route element={<History />} path="/catalogs/history" />
          </Route>
          <Route element={<PrivateRoute role="ADMIN" />}>
            <Route element={<CatalogStudents />} path="/catalogs/component" />
            <Route element={<Users />} path="/catalogs/users" />
          </Route>
          <Route element={<Login />} path="/catalogs/login" />
          <Route element={<Registration />} path="/catalogs/registration" />
        </Routes>
      </div>
      <Footer />
    </BrowserRouter>
  );
}
