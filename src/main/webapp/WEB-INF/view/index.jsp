<!DOCTYPE html>
<html>
<head>
    <title>Group Chat Application </title>
    <link href="styles/bootstrap.min.css" rel="stylesheet">
    <link href="styles/style.css" rel="stylesheet">
    <script src="js/jquery-1.10.2.min.js"></script>
    <script src="js/main.js"></script>
</head>
<body>
<div id="main-content" class="container">
    <div class="row">
        <form class="form-inline">
            <div class="form-group">
                <h1 for="connect">Group Chat</h1>
                <h4>Enter your username</h4>
                <input type="text" name="username" id="username"/>
                <button id="connect" type="button">
                    Connect
                </button>
                <button id="disconnect" type="button" disabled="disabled">
                    Disconnect
                </button>
            </div>
        </form>
    </div>
    <div class="row">
        <div>
            <div id="welcome-text">Loading...</div>
        </div>
        <div class="row">
            <div id="message-container">
                <div class="no-message">No Messages</div>
            </div>
            <div>
                <form class="message-form">
                    <div class="textarea-container">
                        <textarea id="message" placeholder="Write your message here..."></textarea>
                    </div>
                    <div class="send-btn">
                        <button id="send-btn" type="submit">Send</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

</div>
</body>
</html>