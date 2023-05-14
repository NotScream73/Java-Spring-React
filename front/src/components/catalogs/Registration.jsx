import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useEffect } from "react";
import { Link } from 'react-router-dom';
import { useRef } from "react";
export default function Registration(props) {
  const [login, setLogin] = useState("");
  const [password, setPassword] = useState("");
  const [passwordConfirm, setPasswordConfirm] = useState("");
  const navigate = useNavigate();

  useEffect(() => {}, []);

  async function signup() {
    const requestParams = {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        login: login,
        password: password,
        passwordConfirm: passwordConfirm,
      }),
    };
    const response = await fetch(
      "http://localhost:8080/jwt/signup",
      requestParams
    );
    const result = await response.json();
    if (response.status === 200) {
      localStorage.setItem("token", result.token);
      localStorage.setItem("user", result.login);
      localStorage.setItem("role", result.role);
      window.dispatchEvent(new Event("storage"));
      navigate("/catalogs/menu");
    } else {
      localStorage.removeItem("token");
      localStorage.removeItem("user");
      localStorage.removeItem("role");
      alert(result);
    }
  }

  const signupFormOnSubmit = function (event) {
    event.preventDefault();
    signup({
      login: login,
      password: password,
      passwordConfirm: passwordConfirm
    });
  };

  
  return (
    <main className="flex-shrink-0" style={{ backgroundColor: "white" }}>
      <h1 className="my-5 ms-5 ">
        <b>Регистрация</b>
      </h1>
      <form className="row g-3" onSubmit={signupFormOnSubmit}>
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
        <div className="mb-3 row ms-5">
          <label className="col-sm-2 col-form-label" htmlFor="passwordConfirm">
            Пароль
          </label>
          <div className="col-sm-10">
            <input
              placeholder="Подтверждение пароля"
              className="form-control w-50"
              type="password"
              id="passwordConfirm"
              value={passwordConfirm}
              onChange={(e) => setPasswordConfirm(e.target.value)}
            />
          </div>
        </div>
        <h2>
          <button className="btn btn-success ms-5" style={{ color: "black" }}>
            Зарегистрироваться
          </button>
        </h2>
      </form>
    </main>
  );
}
