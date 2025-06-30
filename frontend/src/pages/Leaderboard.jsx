import React, { useState, useEffect } from "react";
import axios from "axios";
import { useLocation, useNavigate } from "react-router-dom";
//this component is used to represent the leaderboard of a specific game to the user.
export default function Leaderboard() {
  const location = useLocation();
  const { gameId } = location.state;
  const navigate = useNavigate();
  const [leaderboard, setLeaderboard] = useState([]);
  const [err, setErr] = useState(null);
  // function to fetch the leaderboard data from the server
  useEffect(() => {
    axios.get(`http://localhost:8080/statistics/leaderboard?gameId=${gameId}`, {
        headers: { Accept: "application/xml" },
        responseType: "document"
      })
      .then((response) => {
        const xml = response.data;
        //reading the xml data and creating an array of entries
        const xmlEntries = Array.from(xml.getElementsByTagName("entry"))
        // looping through the xml entries and creating an array of objects
        const entries = xmlEntries.map((entry) => ({
          username: entry.getElementsByTagName("username")[0]?.textContent,
          wins: entry.getElementsByTagName("wins")[0]?.textContent
        }));
        setLeaderboard(entries);
      })
      .catch((error) => {
        setErr(error)
        console.log("Error fetching leaderboard XML:", error)
});
  }, [gameId]);

  if (err) return <p>Error: {err.message}</p>;

  if (!leaderboard.length) return <p>Loading leaderboard...</p>;

  return (
    <div className="container">
      <h2 className="mt-4">🏆 Leaderboard</h2>
      <table className="table table-bordered mt-3">
        <thead className="table-light">
          <tr>
            <th>Place</th>
            <th>Username</th>
            <th>Wins</th>
          </tr>
        </thead>
        <tbody>
          {leaderboard.map((entry, index) => (
            <tr key={index}>
              <td>
                {index === 0 ? "🥇" : index === 1 ? "🥈" : index === 2 ? "🥉" : index + 1}
              </td>
              <td>{entry.username}</td>
              <td>{entry.wins}</td>
            </tr>
          ))}
        </tbody>
      </table>
      <button className="btn btn-primary" onClick={() => navigate(-1)}>
        Go Back
        </button>
    </div>
  );
}
