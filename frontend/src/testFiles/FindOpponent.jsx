// import React, { useState, useEffect } from "react";
// import { useNavigate } from "react-router-dom";
// import SockJS from "sockjs-client";
// import { Client } from "@stomp/stompjs";

// const FindOpponent = () => {
//   const [waiting, setWaiting] = useState(true);
//   const navigate = useNavigate();

//   useEffect(() => {
//     const client = new Client({
//       webSocketFactory: () => new SockJS("http://localhost:8080/ws"),
//       reconnectDelay: 5000,
//       debug: (str) => console.log("[STOMP Debug]", str),
//       onConnect: () => {
//         // הרשמה לערוץ האישי לקבלת פרטי הסשן
//         client.subscribe("/user/queue/session", (message) => {
//           console.log("WebSocket message received:", message.body);
//           if (message.body) {
//             const sessionData = JSON.parse(message.body);
//             setWaiting(false);
//             // מעבר ישיר למסך המשחק עם פרטי הסשן
//             navigate("/game", { state: sessionData });
//           }
//         });
//       },
//     });
//     client.activate();
//     return () => client.deactivate();
//   }, [navigate]);

//   const requestOpponent = () => {
//     fetch("/api/findOpponent", {
//       method: "POST",
//       headers: { "Content-Type": "application/json" },
//       body: JSON.stringify({ playerId: "currentPlayerId", gameName: "Tic-Tac-Toe" })
//     })
//       .then((response) => response.json())
//       .then((data) => {
//         console.log("REST response:", data);
//         if (data && data.session) {
//           setWaiting(false);
//           // עבור השחקן שהיזום, מעבר ישיר למסך המשחק עם פרטי הסשן
//           navigate("/game", { state: data.session });
//         }
//       });
//   };

//   return (
//     <div>
//       {waiting ? (
//         <div>
//           <p>מחפש יריב...</p>
//           <button onClick={requestOpponent}>חפש יריב</button>
//         </div>
//       ) : (
//         <div>
//           <p>מעביר אותך למשחק...</p>
//         </div>
//       )}
//     </div>
//   );
// };

// export default FindOpponent;
