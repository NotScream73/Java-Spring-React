import { Outlet, Navigate, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
export default function PrivateRoute(props) {
  const navigate = useNavigate();

  useEffect(() => {
    window.addEventListener("storage", () => {
      let token = localStorage.getItem("token");
      if (token) {
        getRole(token).then((role) => {
          if (localStorage.getItem("role") != role) {
            localStorage.removeItem("token");
            localStorage.removeItem("user");
            localStorage.removeItem("role");
            window.dispatchEvent(new Event("storage"));
            navigate("/catalog/main");
          }
        });
      }
    });
  }, []);

  const getRole = async function (token) {
    const requestParams = {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    };
    const requestUrl = `http://localhost:8080/user?token=${token}`;
    const response = await fetch(requestUrl, requestParams);
    const result = await response.text();
    return result;
  };

  let isAllowed = false;
  let userRole = localStorage.getItem("role");
  if (
    props.role === userRole || userRole == "ADMIN"
  ) {
    isAllowed = true;
  }

  return isAllowed ? <Outlet /> : <Navigate to="/catalogs/login" />;
}
