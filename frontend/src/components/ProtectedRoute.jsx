import React from 'react';
import { Navigate } from 'react-router-dom';
// This component is used to protect routes that require authentication. If the user is not logged in, they will be redirected to the login page.
export default function ProtectedRoute({ children }) {
  const isLoggedIn = localStorage.getItem("isLoggedIn") === "true";

  return isLoggedIn ? children : <Navigate to="/" />;
}
