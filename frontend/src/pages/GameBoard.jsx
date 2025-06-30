import React from "react";
import { useLocation } from "react-router-dom";
import TicTacToe from "./TicTacToe";
import FourInRow from "./FourInRow";
// this component is used to create a game board for the application. It contains the game name and the game id, 
// the players ids and the current turn of the game and the players names.
const GameBoard = () => {
  const location = useLocation();
  const {
    sessionId,
    player1Id,
    player2Id,
    currentTurn,
    gameName,
    gameId,
    player1Name,
    player2Name,
  } = location.state;

  const playerId = parseInt(localStorage.getItem("userId"));
  switch (gameName) {
    case "Tic-Tac-Toe":
      return (
        <TicTacToe
          sessionId={sessionId}
          player1Id={player1Id}
          player2Id={player2Id}
          currentTurn={currentTurn}
          playerId={playerId} 
          gameId={gameId}
          player1Name={player1Name}
          player2Name={player2Name}
        />
      );
      break;
      case "4 in a Row":
        return (
          <FourInRow
          sessionId={sessionId}
          player1Id={player1Id}
          player2Id={player2Id}
          currentTurn={currentTurn}
          playerId={playerId}
          gameId={gameId}
          player1Name={player1Name}
          player2Name={player2Name}
    />
        );
      break;
    default:
      return <h2>⛔game "{gameName}" is not supported right now</h2>;
  }
}

export default GameBoard;
