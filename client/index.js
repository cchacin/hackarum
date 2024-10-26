const webSocket = new WebSocket("ws://192.168.0.52:8080/lobby/ricardo");
let webSocketGame;
let username;
// connetion to the server
webSocket.onopen = function (e) {
  console.log("Connected to server");
};
const createNewUser = () => {
  if (localStorage.getItem("username") === null) {
      username = prompt("Please enter your username", "User");
    localStorage.setItem("username", username);
  }
};
createNewUser();


// message sending and receiving
webSocket.onmessage = function (e) {
  console.log(e.data);
  const data = JSON.parse(e.data);
  const gameID = data.data.gameId;
  if (gameID) {
    localStorage.setItem("gameId", gameID);
    console.log("game id saved");
    try {
      webSocketGame = new WebSocket(`ws://192.168.0.52:8080/game/${gameID}/user/${username}`);
    //   console.log(webSocketGame)
    } catch (error) {
      console.log(error);
    }
  }
};

// error handling
webSocket.onerror = function (error) {
  alert(`Tring to connect to ${error.currentTarget.url} type: ${error.type}`);
};

const create = () => {
  console.log("Creating a new user");
  webSocket.send(JSON.stringify({ action: "CREATE", data: {} }));
};

const join = () => {
  console.log("Joining the lobby");
  webSocket.send(JSON.stringify({ action: "JOIN", data: {} }));
};
