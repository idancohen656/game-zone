import React, { useEffect, useState } from "react";
import axios from "axios";
// this component is used to represent the home page of the application. It contains the overall stats of the user.
export default function Home() {
  const [stats, setStats] = useState(null);
  const userId = Number(localStorage.getItem("userId"))
  const name =localStorage.getItem("username")
  const [error, setError] = useState(null);
  // function to fetch the overall stats of the user from the server
  useEffect(() => {
    axios
      .get(`http://localhost:8080/statistics/summary/total?userId=${userId}`, {
        headers: { Accept: "application/xml" },
        responseType: "document"
      })
      .then((response) => {
        const xml = response.data;

        const playerId = xml.getElementsByTagName("player1Id")[0]?.textContent;
        const totalWins = xml.getElementsByTagName("totalWins")[0]?.textContent;
        const losses = xml.getElementsByTagName("losses")[0]?.textContent;
        const draws = xml.getElementsByTagName("draws")[0]?.textContent;


        setStats({ playerId, totalWins, losses, draws});
      })
      .catch((err) => {
        setError(err);
        console.error("Error fetching summary XML:", error);
      });
  }, [userId]);
  if (!stats) return <p>Loading stats...</p>;

  return (
    <div className="container mt-4" >
      <h2 > Home</h2>
      <p>👤Welcome, <strong>{name}</strong>!👤</p>
      <h2 >📊Your Overall Stats📊</h2>
      <p>👍🏽 Total Wins: <strong>{stats.totalWins}👍🏽</strong></p>
      <p>🫱🏽‍🫲🏽 Total Draws: <strong>{stats.draws}🫱🏽‍🫲🏽</strong></p>
      <p>👎🏽 Total Losses: <strong>{stats.losses}👎🏽</strong></p>

    </div>
  );
}
