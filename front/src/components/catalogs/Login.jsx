import { useState, useEffect } from "react";
import { Link, useNavigate } from 'react-router-dom';
import { useRef } from "react";
export default function Login(props) {
  const [login, setLogin] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
  }, []);

  const loginsystem = async function (login, password) {
      const requestParams = {
          method: "POST",
          headers: {
              "Content-Type": "application/json",
          },
          body: JSON.stringify({login: login, password: password}),
      };
      const response = await fetch("http://localhost:8080/jwt/login", requestParams);
      const result = await response.text();
      if (response.status === 200) {
          localStorage.setItem("token", result);
          localStorage.setItem("user", login);
          getRole(result);
      } else {
          localStorage.removeItem("token");
          localStorage.removeItem("user");
          localStorage.removeItem("role");
      }
  }

  const getRole = async function (token) {
      const requestParams = {
          method: "GET",
          headers: {
              "Content-Type": "application/json"
          }
      };
      const requestUrl = `http://localhost:8080/user?token=${token}`;
      const response = await fetch(requestUrl, requestParams);
      const result = await response.text();
      localStorage.setItem("role", result);
      window.dispatchEvent(new Event("storage"));
      navigate("/main");
  }

  const loginFormOnSubmit = function (event) {
      event.preventDefault();
      loginsystem(login, password);
  };












  return (
    <main className="flex-shrink-0" style={{ backgroundColor: "white" }}>
      <h1 className="my-5 ms-5 ">
        <b>Вход в систему</b>
      </h1>
      <form className="row g-3" onSubmit={loginFormOnSubmit}>
        <div className="mb-3 row ms-5">
          <label className="col-sm-2 col-form-label" htmlFor="login">
            Логин
          </label>
          <div className="form-outline col-sm-10">
            <input
              placeholder="Логин"
              className="form-control w-50"
              type="text"
              id="login"
              name="login"
              value={login}
              onChange={(e) => setLogin(e.target.value)}
            />
          </div>
        </div>
        <div className="mb-3 row ms-5">
          <label className="col-sm-2 col-form-label" htmlFor="password">
            Пароль
          </label>
          <div className="col-sm-10">
            <input
              placeholder="Пароль"
              className="form-control w-50"
              type="password"
              id="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
          </div>
        </div>
        <h2>
          <button className="btn btn-success ms-5" style={{ color: "black" }}>
            Войти
          </button>
        </h2>
      </form>
    </main>
  );
}
