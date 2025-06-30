import React, { useEffect, useState } from "react";
import webSocketService from "../Services/WebSocketService";
import "../cssFiles/FourInRowCss.css";
import { useNavigate } from "react-router-dom";
// this component is used to represent the four in a row game board to the user.
export default function FourInRow({
  sessionId,
  player1Id,
  player2Id,
  currentTurn,
  playerId,
  gameId,
  player1Name,
  player2Name,
}) {
  const navigate = useNavigate();
  const [gameState, setGameState] = useState(null);
  const [currentPlayerName, setCurrentPlayer] = useState(null);
  // this state is used to store the current player name
  useEffect(()=>{
      if (gameState){
      setCurrentPlayer(gameState.nextPlayer==player1Id?player1Name:player2Name)
      }
    },[gameState,  player1Id, player1Name, player2Name]);

  const goHome = () => {navigate("/home");}

  const handleRematch = () => {
    navigate(`/games/${gameId}`, {
      state: { playerId },
    });
  };
    // this function is used to establish a connection with the server and subscribe to the game state updates
  useEffect(() => {
    let connected = false;
    webSocketService.connect(() => {
      connected = true;
      webSocketService.subscribe(`/topic/game/${sessionId}`, (data) => {
        setGameState(data);
      });
        // notify the server that the player is ready to play
      webSocketService.send("/app/four-in-row/start", {
        sessionId,
        playerId: currentTurn,
      });
    });
    // this function is used to handle the disconnection of the player from the game
    const handleDisconnect = () => {
      if (connected) {
        webSocketService.send("/app/four-in-row/disconnect", {
          sessionId,
          playerId,
        });
        webSocketService.disconnect();
      }
    };
    // this function is used to handle the beforeunload event and send a disconnect message to the server
    window.addEventListener("beforeunload", handleDisconnect);
    return () => {
      handleDisconnect();
      window.removeEventListener("beforeunload", handleDisconnect);
    };
  }, [sessionId, currentTurn, playerId]);
  // this function is used to handle the click event on the column buttons and send a move message to the server
  const handleColumnClick = (columnIndex) => {
    if (!gameState || gameState.nextPlayer !== playerId || gameState.status !== "IN_PROGRESS") {
      alert("It's not your turn or the game has ended");
      return;
    }
    webSocketService.send("/app/four-in-row/move", {
      sessionId,
      playerId,
      column: columnIndex,
    });
  };
  //this function used to render the game board cells and their values
  const renderCell = (row, col) => {
    const index = row * 7 + col;
    const value = gameState.board[index];
    const isWinning = gameState.winningCells?.includes(index);
    return (
      <div
        key={`${row}-${col}`}
        className={`fourcell ${value === "X" ? "x" : value === "O" ? "o" : ""} ${isWinning ? "winning" : ""}`}
      >
        {value !== "-" ? value : ""}
      </div>
    );
  };
    // this function is used to render the game board and its cells
  const renderBoard = () => {
    const rows = [];
    for (let row = 0; row < 6; row++) {
      const cells = [];
      for (let col = 0; col < 7; col++) {
        cells.push(renderCell(row, col));
      }
      rows.push(
        <div key={row} className="fourrow">
          {cells}
        </div>
      );
    }
    return <div className="fourboard">{rows}</div>;
  };
    // this function is used to render the column buttons for the game board
  const renderColumnButtons = () => {
    return (
      <div className="fourcolumn-select">
        {Array.from({ length: 7 }, (_, col) => (
          <button key={col} className="fourcolumn-button" onClick={() => handleColumnClick(col)}>
            ↓
          </button>
        ))}
      </div>
    );
  };

  return (
    <div className="container text-center">
      <h1>Four In Row</h1>
      <p>
        Player 1: {player1Name} | Player 2: {player2Name}
      </p>
      <p>It's {currentPlayerName}'s turn</p>
      {gameState ? (
        <>
          {renderColumnButtons()}
          {renderBoard()}
          {gameState.status === "FINISHED" && (
            <h2>🏆 Winner: {gameState.winner}</h2>
          )}
          {gameState.status === "DRAW" && <h2>🤝 It's a draw!</h2>}
          {["FINISHED", "DRAW"].includes(gameState.status) && (
            <div className="mt-3 d-flex justify-content-center gap-2">
              <button className="btn btn-primary" onClick={handleRematch}>
                Play Again
              </button>
              <button className="btn btn-primary" onClick={goHome}>
                Go Home
              </button>
            </div>
          )}
        </>
      ) : (
        <p>Waiting for game state from server...</p>
      )}
    </div>
  );
}
