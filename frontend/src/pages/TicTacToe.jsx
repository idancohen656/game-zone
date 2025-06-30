import React, { useEffect, useState } from "react";
import webSocketService from "../Services/WebSocketService";
import "../cssFiles/TicTacToeCss.css";
import { useNavigate } from "react-router-dom";
// this component is used to represent the tic tac toe game board to the user.
const TicTacToe = ({
  sessionId,
  player1Id,
  player2Id,
  currentTurn,
  playerId,
  gameId,
  player1Name,
  player2Name,
}) => {
  const navigate = useNavigate();
  const [gameState, setGameState] = useState(null);
  const [currentPlayerName, setCurrentPlayer]=useState(null)
  // this state is used to store the current player name
  useEffect(()=>{
    if (gameState){
    setCurrentPlayer(gameState.nextPlayer==player1Id?player1Name:player2Name)
    }
  },[gameState,  player1Id, player1Name, player2Name]
)

 const goHome = () => {
    navigate("/home");
  };

  const handleRematch = () => {
    navigate(`/games/${gameId}`, {
      state: { playerId },
    });
  };
  // this function is uded to establish a connection with the server and subscribe to the game state updates
  useEffect(() => {
    let connected = false;
    webSocketService.connect(() => {
      connected = true;
      // subscribe to the game state updates
      webSocketService.subscribe(`/topic/game/${sessionId}`, (data) => {
        setGameState(data);
      });
      // notify the server that the player is ready to play
      webSocketService.send("/app/tic-tac-toe/start", {
        sessionId,
        playerId: currentTurn,
      });
    });
    // this function is used to handle the disconnection of the player from the game
    const handleDisconnect = () => {
      if (connected) {
        console.log("🔌 sending disconnect message...");
        webSocketService.send("/app/tic-tac-toe/disconnect", {
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
  // this function is used to handle the click event on the cell of the game board
  const handleCellClick = (index) => {
    if(gameState.nextPlayer !== playerId)
      alert("it's opponent's turn!")
    else if (gameState.status === "FINISHED") {
      alert("Game is already finished, start new one!")
      return;
    }
    else if (
      gameState.board[index] !== "-"
    ) {
      alert("this sit is already taken!")
      return;
    }
    // send the move to the server
    webSocketService.send("/app/tic-tac-toe/move", {
      sessionId,
      playerId,
      cellIndex: index,
    });
  };
  // this function is used to render the cell of the game board
  const renderCell = (index) => {
    const value = gameState.board[index];
    
    return (
      <button
        key={index}
        className={`cell ${value === 'X' ? 'x' : value === 'O' ? 'o' : ''}`}
        onClick={() => handleCellClick(index)}
      >
        {value === "-" ? "" : value}
      </button>
    );
  };
  // this function is used to render the game board
  const renderBoard = () => (
    <div className="board">
      {gameState.board.map((_, index) => renderCell(index))}
    </div>
  );
  
  return (
    <div className="container">
      <h1>Tic-Tac-Toe</h1>
      <p>
        Player 1: {player1Name} | Player 2: {player2Name}
      </p>
      <p>It's {currentPlayerName}'s turn</p>
            {gameState ? (
        <>
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
};

export default TicTacToe;
