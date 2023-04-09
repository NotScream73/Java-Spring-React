import './App.css';
import { useRoutes, Outlet, BrowserRouter } from 'react-router-dom';
import Header from './components/common/Header';
import Footer from "./components/common/Footer";
import CatalogStudents from './components/catalogs/CatalogStudents';
import Menu from './components/catalogs/Menu';
import Basket from './components/catalogs/Basket';
import { useState } from 'react';

function Router(props) {
  return useRoutes(props.rootRoute);
}

export default function App() {
  const [product,setProduct] = useState([]);
  const routes = [
    { index: true, element: <CatalogStudents /> },
    { path: "catalogs/menu", element: <Menu product={product} setProduct={setProduct}/>, label: "Меню" },
    { path: "catalogs/component", element: <CatalogStudents />, label: "Компоненты" },
    { path: "catalogs/basket", element: <Basket product={product} setProduct={setProduct}/>, label: "Корзина" }
  ];
  const links = routes.filter(route => route.hasOwnProperty('label'));
  const rootRoute = [
    { path: '/', element: render(links), children: routes }
  ];

  function render(links) {
    return (
      <>
        <Header links={links} />
        <div className="container-fluid p-0">
          <Outlet />
        </div>
        <Footer></Footer>
      </>
    );
  }

  return (
    <BrowserRouter>
      <Router rootRoute={ rootRoute } />
    </BrowserRouter>
  );
}