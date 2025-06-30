import { useNavigate } from "react-router-dom";
// this component is used to represent the welcome page of the application. It contains the logo and the description of the application.
export default function Welcome() {
  const navigate = useNavigate();
  return (
    <div className="container d-flex flex-column justify-content-center align-items-center vh-100 text-center">
      <h1>Welcome to Game Zone</h1>
      <img src="/gameZoneLogo.webp" alt="logo" width={250} />
      <p>
        Game Zone is the ultimate platform for discovering, playing, and enjoying a variety of games. 
        Whether you’re into board games, strategy games, or fast-paced challenges, we have something for everyone!
    </p>
    <h2>What We Offer</h2>
    <ul>
        <li>🎮 A diverse collection of interactive games</li>
        <li>👥 Play and compete with friends</li>
        <li>📊 Track your statistics and improve your skills</li>
        <li>🏆 Climb the leaderboard and become a champion</li>
    </ul>
    
    <h2>Join Us!</h2>
    <p>
        Sign up today, start playing, and be part of the Game Zone community!
    </p>
      <button className="btn btn-primary mb-2" onClick={() => navigate("/login")}>
        Login
      </button>
      <button className="btn btn-secondary mb-2" onClick={() => navigate("/register")}>
        Register
      </button>
    </div>
  );
}
