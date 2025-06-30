import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";
// This is a WebSocket service that uses STOMP over SockJS to connect to a WebSocket server.
class WebSocketService {
  constructor() {
    this.client = null;
  }
  // This method connects to the WebSocket server and sets up the STOMP client.
  connect(onMessage) {
    this.client = new Client({
      webSocketFactory: () => new SockJS("http://localhost:8080/ws"),
      onConnect: () => {
        console.log("WebSocket Connected");
        onMessage(); 
      },
      onStompError: (frame) => {
        console.error("STOMP Error:", frame);
      },
    });

    this.client.activate();
  }
  // This method subscribes to a specific destination and sets up a callback to handle incoming messages.
  subscribe(destination, callback) {
    this.client.subscribe(destination, (message) => {
      const data = JSON.parse(message.body);
      callback(data);
    });
  }
  // This method sends a message to a specific destination with the provided payload.
  send(destination, payload) {
    this.client.publish({
      destination: destination,
      body: JSON.stringify(payload),
    });
  }
  // This method disconnects the STOMP client from the WebSocket server.
  disconnect() {
    if (this.client) {
      this.client.deactivate();
    }
  }
}
// This is an instance of the WebSocketService class that can be used throughout the application.
const webSocketService = new WebSocketService();
export default webSocketService;
