/* This file contains the application's main component, which sets up the routing and state management for the application.
It uses React Router for navigation */
import React, { useState } from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Home from "./pages/Home/.";
import Games from "./pages/Games/.";
import About from "./pages/About/.";
import Login from "./pages/Login/.";
import Register from "./pages/Register/.";
import Navbar from "./components/Navbar";
import GameDetails from "./pages/GamesDetails";
import ProtectedRoute from "./components/ProtectedRoute";
import Welcome from './pages/Welcome';
import TicTacToe from './pages/TicTacToe';
import FourInRow from './pages/FourInRow';
import GameBoard from './pages/GameBoard';
import Leaderboard from "./pages/Leaderboard";


function App() {
    const [isLoggedIn, setIsLoggedIn] = useState(
        localStorage.getItem("isLoggedIn") === "true"
    );

    return (
        <Router>
            {isLoggedIn && <Navbar setIsLoggedIn={setIsLoggedIn} />}
            <Routes>
                <Route path="/" element={<Welcome />} />
                <Route path="/home" element={
                    <ProtectedRoute>
                        <Home />
                    </ProtectedRoute>
                } />
                <Route path="/games" element={
                    <ProtectedRoute>
                        <Games />
                    </ProtectedRoute>
                } />
                <Route path="/about" element={
                    <ProtectedRoute>
                        <About />
                    </ProtectedRoute>
                } />
                <Route path="/games/:id" element={
                    <ProtectedRoute>
                        <GameDetails />
                    </ProtectedRoute>
                } />
                <Route path="/statistics/leaderboard" element={
                    <ProtectedRoute>
                        <Leaderboard />
                    </ProtectedRoute>
                } />
                <Route path="/login" element={<Login setIsLoggedIn={setIsLoggedIn} />} />
                <Route path="/register" element={<Register setIsLoggedIn={setIsLoggedIn} />} />
                <Route path="/game/:gameName/session/:sessionId" element={
                    <protectedRoute>
                    <TicTacToe />
                    </protectedRoute>
                    } />
                <Route path="/game/Four in Row/session/:sessionId" element={
                    <protectedRoute>
                    <FourInRow />
                    </protectedRoute>} />
                <Route path="/gameBoard" element={
                    <ProtectedRoute>
                    <GameBoard />
                    </ProtectedRoute>
                    } />
            </Routes>
        </Router>
    );
}

export default App;
