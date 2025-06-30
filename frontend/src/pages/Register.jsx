import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { Icon } from "react-icons-kit";
import { eyeOff } from "react-icons-kit/feather/eyeOff";
import { eye } from "react-icons-kit/feather/eye";

// this component is used to represent the register page of the application.
export default function Register({ setIsLoggedIn }) {
  const navigate = useNavigate();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [email, setEmail] = useState("");
  const [showPassword, setShowPassword] = useState(false);

  const handleToggle = () => {
    setShowPassword(!showPassword);
  };
  // this function is used to handle the register button click event
  const handleRegister = () => {
    // Validate input fields
    if (!username || !password || !email) {
      alert("Please fill in all fields.");
      return;
    }
    // Validate username
    const usernameRegex = /^[a-zA-Z0-9_]{3,}$/;
    if (!usernameRegex.test(username)) {
      alert("Username must be at least 3 characters and contain only letters, numbers, or underscores.");
      return;
    }
    // Validate password
    if (password.length < 6 || !/\d/.test(password) || !/[a-zA-Z]/.test(password)) {
      alert("Password must be at least 6 characters long and contain both letters and numbers.");
      return;
    }
    // Validate email
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
      alert("Please enter a valid email address.");
      return;
    }
    // Sending a request to the server to register the user with the provided username, password, and email
    axios.post("http://localhost:8080/auth/register", {
        username,
        password,
        email,
      })
      .then((response) => {
        alert("Registration successful!");
        localStorage.setItem("isLoggedIn", "true");
        localStorage.setItem("username", response.data.username);
        localStorage.setItem("userId", response.data.userId);
        setIsLoggedIn(true);
        navigate("/home");
      })
      .catch((error) => {
        alert("Registration failed. Please try again.");
        console.error("Error:", error);
      });
  };

  return (
    <div className="container d-flex flex-column justify-content-center align-items-center vh-100 text-center">
      <h1>Register to Game Zone</h1>

      <input
        type="text"
        placeholder="Username"
        className="form-control w-50 mb-3"
        value={username}
        onChange={(e) => setUsername(e.target.value)}
      />

      <div className="position-relative w-50 mb-3">
        <input
          type={showPassword ? "text" : "password"}
          placeholder="Password"
          className="form-control pe-5"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        {/* Eye icon to toggle password visibility */}
        <span
          className="position-absolute end-0 top-50 translate-middle-y me-3"
          style={{ cursor: "pointer" }}
          onClick={handleToggle}
        >
          <Icon icon={showPassword ? eye : eyeOff} size={20} className="text-dark" />
        </span>
      </div>

      <input
        type="email"
        placeholder="Email"
        className="form-control w-50 mb-3"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
      />

      <button className="btn btn-success mb-2" onClick={handleRegister}>
        Register
      </button>
      <button className="btn btn-secondary" onClick={() => navigate(-1)}>
        Go back
      </button>
    </div>
  );
}
