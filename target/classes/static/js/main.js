let ws;
let username;
let messageId = 0;

function setConnected(connected) {
    console.log("setConnected");
    if (connected) {
        $("#connect").hide();
        $("#disconnect").show();
    } else {
        $("#connect").show();
        $("#disconnect").hide();
    }
}

function connect() {
    ws = new WebSocket("ws://localhost:9999");
    console.log(ws)
    ws.onopen = () => {
        console.log("connected");
        setConnected(true);
    };
    ws.onmessage = function (data) {
        let dataObj = JSON.parse(data.data);
        if (dataObj.username)
            handleMessage(dataObj.username, dataObj.message);
    }
    ws.onclose = function (evt) {
        alert('Bye!')
    };
    ws.onerror = error => {
        alert("Error occurred while connecting!")
    };
}

function disconnect() {
    if (ws != null) {
        ws.close();
    }
    setConnected(false);
    console.log("Websocket is in disconnected state");
}

function sendData() {
    if (!ws) {
        alert("You are not connected!");
        return;
    }
    let message = $("#message").val();
    if(!message){
        alert("Message cannot be empty!");
        return;
    }
    let data = JSON.stringify({
        username: username ? username : "anonymous",
        message: message
    })
    handleMessage("You", message);
    ws.send(data);
    $("#message").val("");
}

function handleMessage(senderUsername, message) {
    if (messageId === 0) {
        $("#message-container").html("");
    }
    $("#message-container").append(`<div id="'message'+messageId">
        <span class='sender'>${senderUsername}</span>
        <td>${message}</td>
        </div>`);
    messageId++;
}

function getWelcomeText() {
    if (username) return "Welcome " + username;
    return "Welcome";
}

function setWelcomeText() {
    $("#welcome-text").html(getWelcomeText())
}

$(function () {
    setConnected(false);
    setWelcomeText();
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#connect").click(function () {
        connect();
    });
    $("#disconnect").click(function () {
        disconnect();
    });
    $("#send-btn").click(function () {
        sendData();
    });
    $("#username").on("change", function (inputFieldRef) {
        username = inputFieldRef.target.value;
        setWelcomeText();
    });
});