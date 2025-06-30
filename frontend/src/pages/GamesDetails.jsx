import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import "../App.css";
// this component is used to represent the game details to the user.
const GameDetails = () => {
    const navigate = useNavigate();
    const { id } = useParams();
    const [game, setGame] = useState(null);
    const [waiting, setWaiting] = useState(false);
    const [opponentResponse, setOpponentResponse] = useState(null);
    const [error, setError] = useState(null);
    // function to handle the find opponent button click event
    const handleFindOpponent = () => {

        const playerId = localStorage.getItem("userId");
        //sendeing a request to the server to find an opponent for the player
        axios.post(`http://localhost:8080/games/findOpponent`, null, {
            params: { playerId, gameName: game.name }
        }).then(response => {
            if (response.data) {
                // if an opponent is found, navigate to the game board
                setOpponentResponse(response.data);
            } else {
                // if no opponent is found, set the waiting state to true
                setWaiting(true);
            }
        }).catch(error => {
            console.error("Error finding opponent:", error);
        });
    };
    // function to navigate to the leaderboard page for specific game
    const goToLeaderBoard = (id) =>{
        navigate(`/statistics/leaderboard`, {
            state: { gameId: id },
        });
    }
    // retrieve the game details from the server
    useEffect(() => {
        axios.get(`http://localhost:8080/games/${id}`)
            .then(response => {
                setGame(response.data);
            })
            .catch(error => {
                console.error("Error fetching game details:", error);
            });
    }, [id]);
    // check if the player is waiting for an opponent every 2 seconds
    useEffect(() => {
        if (waiting) {
            const interval = setInterval(() => {
                const playerId = localStorage.getItem("userId");
                axios.post(`http://localhost:8080/games/findOpponent`, null, {
                    params: { playerId, gameName: game.name }
                }).then(response => {
                    if (response.data) {
                        setOpponentResponse(response.data);
                        clearInterval(interval);
                    }
                }).catch(() => {
                    setError("Error checking for opponent.");
                    console.log("Error checking for opponent:", error);
                 });
            }, 2000);
            return () => clearInterval(interval);
        }
    }, [waiting, game]);
    // navigate to the game board when an opponent is found
    useEffect(() => {
        if (opponentResponse) {
            navigate("/gameBoard", {
                state: {
                  sessionId: opponentResponse.sessionId,
                  player1Id: localStorage.getItem("userId"), 
                  player2Id: opponentResponse.opponentId,
                  currentTurn: opponentResponse.currentTurn || localStorage.getItem("userId"), 
                  player1Name: opponentResponse.player1Name,
                  player2Name: opponentResponse.player2Name,
                  gameName: game.name,
                  gameId: id,
                },
              });
        }
    }, [opponentResponse, navigate, game, id]);

    return (
        <div className="container">
            {game ? (
                <>
                    <h1>{game.name}</h1>
                    <p className="test">Description</p>
                    <p>{game.description}</p>
                    <p className="test">Rules</p>
                    <p>{game.rules}</p>
                    <p className="test2">⚔️ Let the games begin! ⚔️</p>
                    <button className="btn btn-primary" onClick={handleFindOpponent}>
                        {waiting ? "Waiting for opponent..." : "Find Opponent"}
                    </button>
                    <button className="btn btn-primary" onClick={()=>goToLeaderBoard(id)}>
                        Leaderboard
                    </button>
                </>
            ) : (
                <p>Loading game details...</p>
            )}
        </div>
    );
};

export default GameDetails;
