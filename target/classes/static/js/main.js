var ws;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
}

function connect() {
    // return new Promise((resolve, reject) => {
    ws = new WebSocket("ws://localhost:9999");
    console.log(ws)
    ws.onopen = () => {
        console.log("connected");
        setConnected(true);
    };
    ws.onmessage = function (data) {
        helloWorld(data.data);
    }
    ws.onclose = function (evt) {
        console.log("I'm sorry. Bye!");
    };
    ws.onerror = error => {
        console.error("ERROR while connecting to the socket")
    };
    // });
}

function disconnect() {
    if (ws != null) {
        ws.close();
    }
    setConnected(false);
    console.log("Websocket is in disconnected state");
}

function sendData() {
    var data = JSON.stringify({
        'user': $("#user").val()
    })
    ws.send(data);
}

function helloWorld(message) {
    $("#helloworldmessage").append(" " + message + "");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#connect").click(function () {
        connect();
    });
    $("#disconnect").click(function () {
        disconnect();
    });
    $("#send").click(function () {
        sendData();
    });
});