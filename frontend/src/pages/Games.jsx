import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import axios from "axios";
// this component is used to represent the games list to the user.
const Games = () => {
  const [games, setGames] = useState([]);
  // retrieve the games list from the server
  useEffect(() => {
    axios
      .get("http://localhost:8080/games")
      .then((response) => {
        setGames(response.data);
      })
      .catch((error) => {
        console.error("Error fetching games:", error);
      });
  }, []);
  // function to get the image for the game based on its name
  const getImageForGame = (gameName) => {
    const name = gameName.toLowerCase();
  
    if (name.includes("checkers")) return "/images/checkers.webp";
    if (name.includes("row")) return "/images/4inRow.webp";
    if (name.includes("rock")) return "/images/rock.webp";
    if (name.includes("war")) return "/images/war.webp";
    if (name.includes("tic")) return "/images/Tic-Tac-Toe.webp";
    if (name.includes("memory")) return "/images/memory.webp";
  
    return "/images/default.webp";
  };
  
  

  return (
    <div className="container mt-5">
      <h2 className="mb-4 text-center">Available Games 🎮</h2>
      {games.length > 0 ? (
        <div className="row">
          {games.map((game) => ( // loop through the games list and create a card for each game
            <div className="col-md-4 mb-4 text-center" key={game.id}>
              {/*the className card is from bootstrap */}
              <div className="card h-100 shadow"> 
                {/* the image in each card taken by names */}
                <img
                  src={getImageForGame(game.name) }
                  className="card-img-top"
                  alt={game.name}
                  style={{ height: "200px", objectFit: "cover" }}
                />
                <div className="card-body d-flex flex-column justify-content-between">
                  <h5 className="card-title">{game.name}</h5>
                  <Link to={`/games/${game.id}`} className="btn btn-primary mt-auto">
                    View Game
                  </Link>
                </div>
              </div>
            </div>
          ))}
        </div>
      ) : (
        <p className="text-center">No games found...</p>
      )}
    </div>
  );
};

export default Games;
