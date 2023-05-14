import { useState } from "react";
import { useEffect } from "react";
import DataService from "../../services/DataService";

export default function Users(props) {
  const [users, setUsers] = useState([]);
  const [pageNumbers, setPageNumbers] = useState([]);
  const [pageNumber, setPageNumber] = useState();
  const usersUrl = "/users";
  const host = "http://localhost:8080";

  useEffect(() => {
    DataService.readUsersPage(host, usersUrl, 1).then((data) => {
      setUsers(data.users.content);
      setPageNumbers(data.pageNumbers);
      setPageNumber(1);
    });
  }, []);
  const pageButtonOnClick = function (page) {
    DataService.readUsersPage(host, usersUrl, page).then((data) => {
      setUsers(data.users.content);
      setPageNumber(page);
    });
  };
  return (
    <>
    <main className="flex-shrink-0" style={{ backgroundColor: "white" }}>
      <div className="table-shell mb-3">
        <table className="table">
          <thead>
            <tr>
              <th style={{ width: "15%" }} scope="col">
                ID
              </th>
              <th style={{ width: "30%" }} scope="col">
                Логин
              </th>
              <th style={{ width: "15%" }} scope="col">
                Роль
              </th>
            </tr>
          </thead>
          <tbody>
            {users.map((user, index) => (
              <tr key={index}>
                <td style={{ width: "15%" }}>{user.id}</td>
                <td style={{ width: "30%" }}>{user.login}</td>
                <td style={{ width: "15%" }}>{user.role}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      <div>
        <p>Pages:</p>
        <nav>
          <ul className="pagination" style={{ backgroundColor: "white" }}>
            {pageNumbers.map((number, index) => (
              <li key={index}
                className={`page-item ${
                  number === pageNumber ? "active" : ""
                }`}
                onClick={() => pageButtonOnClick(number)}
              >
                <a className="page-link" >
                  {number}
                </a>
              </li>
            ))}
          </ul>
        </nav>
      </div>
      </main> 
    </>
  );
}
