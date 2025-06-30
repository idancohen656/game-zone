import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { Icon } from "react-icons-kit";
import { eyeOff } from "react-icons-kit/feather/eyeOff";
import { eye } from "react-icons-kit/feather/eye";
// this component is used to represent the login page of the application. 
export default function Login({ setIsLoggedIn }) {
  const navigate = useNavigate();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [type, setType] = useState("password");
  const [icon, setIcon] = useState(eyeOff);
  // this function is used to toggle the password visibility
  const handleToggle = () => {
    setType(type === "password" ? "text" : "password");
    setIcon(type === "password" ? eye : eyeOff);
  };
  // this function is used to handle the login button click event
  const handleLogin = () => {
    if (!username || !password) {
      alert("Please enter username and password");
      return;
    }
    // sending a request to the server to login the user with the provided username and password
    axios.post("http://localhost:8080/auth/login", {
        username,
        password,
      })
      .then((response) => {
        localStorage.setItem("isLoggedIn", "true");
        localStorage.setItem("username", response.data.username);
        localStorage.setItem("userId", response.data.userId);
        setIsLoggedIn(true);
        navigate("/home");
      })
      .catch((error) => {
        if (error.response?.status === 401) {
          alert("Invalid username or password. Please try again.");
        } else {
          alert("Login failed. Please try again.");
          console.error("Error:", error);
        }
      });
  };

  return (
    <div className="container d-flex flex-column justify-content-center align-items-center vh-100 text-center">
      <h1>Login to Game Zone</h1>
      <input
        type="text"
        placeholder="Username"
        className="form-control w-50 mb-3"
        value={username}
        onChange={(e) => setUsername(e.target.value)}
      />

      <div className="position-relative w-50 mb-3">
        <input
          type={type}
          placeholder="Password"
          className="form-control pe-5"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <span
          className="position-absolute end-0 top-50 translate-middle-y me-3"
          style={{ cursor: "pointer" }}
          onClick={handleToggle}
        >
          <Icon icon={icon} size={20} className="text-dark"/>
        </span>
      </div>

      <button className="btn btn-primary mb-2" onClick={handleLogin}>
        Login
      </button>
      <button className="btn btn-secondary" onClick={() => navigate(-1)}>
        Go back
      </button>
    </div>
  );
}
