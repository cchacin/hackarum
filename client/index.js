let webSocketGame;
let username;
const createNewUser = () => {
  if (localStorage.getItem("username") === null) {
    username = prompt("Please enter your username", "User");
    localStorage.setItem("username", username);
  } else {
    username = localStorage.getItem("username");
  }
};
createNewUser();

// create a new websocket
const webSocket = new WebSocket(`ws://192.168.0.52:8080/lobby/${username}`);

// connetion to the server

webSocket.onopen = function (e) {
  console.log("Connected to server");
};

function hideElement(elementId) {
  let el = document.getElementById(elementId).classList;
  el.add("hidden");
  el.remove("show");
}

function showElement(elementId) {
  let el = document.getElementById(elementId).classList;
  el.add("show");
  el.remove("hidden");
}

// message sending and receiving
webSocket.onmessage = function (e) {
  const data = JSON.parse(e.data);
  if (data.action === "MESSAGE") {
    document.getElementById("lobby-messages").innerText = data.data.message;
  }
  if (data.action === "CREATED") {
    console.log("Creating a new game");
    const gameID = data.data.gameId;
    if (gameID) {
      hideElement("lobby-actions");
      showElement("game");
      localStorage.setItem("gameId", gameID);
      console.log("game id saved");
      try {
        webSocketGame = new WebSocket(
          `ws://192.168.0.52:8080/game/${gameID}/user/${username}`
        );
        console.log(webSocketGame);
      } catch (error) {
        console.log(error);
      }
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
