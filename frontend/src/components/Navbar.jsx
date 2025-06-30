import React from "react";
import { Link, useNavigate } from "react-router-dom";
import 'bootstrap-icons/font/bootstrap-icons.css';
// this component is used to create a navbar for the application. It contains links to different pages and a logout button.
export default function Navbar({ setIsLoggedIn }) {
  const navigate = useNavigate();
  const username = localStorage.getItem("username"); 
  // function to handle logout click event
  const handleLogout = () => {
    localStorage.setItem("isLoggedIn", "false");
    localStorage.removeItem("username");
    localStorage.removeItem("userId");
    setIsLoggedIn(false);
    navigate("/welcome");
  };

  return (
    <nav className="navbar navbar-expand-lg navbar-dark bg-dark px-4">
      <div className="container-fluid">
        <Link className="navbar-brand" to="/home">
          <img src="/gameZoneLogo.webp" alt="logo" width={100} />
        </Link>
        <div className="d-flex align-items-center ms-auto gap-3">
  <ul className="navbar-nav d-flex flex-row mb-0">
    <li className="nav-item me-3">
      <Link className="nav-link" to="/home">Home</Link>
    </li>
    <li className="nav-item me-3">
      <Link className="nav-link" to="/games">Games</Link>
    </li>
    <li className="nav-item me-3">
      <Link className="nav-link" to="/about">About</Link>
    </li>
  </ul>

  <span className="text-white">
    Hello, <strong>{username}</strong>
  </span>

  <button
    onClick={handleLogout}
    className="btn btn-outline-light"
    title="Logout"
  >
    <i className="bi bi-box-arrow-right me-2"></i> Logout
  </button>
</div>

      </div>
    </nav>
  );
}