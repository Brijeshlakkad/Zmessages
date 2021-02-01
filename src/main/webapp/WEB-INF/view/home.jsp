<!DOCTYPE html>
<html>
<head>
    <title>WebSocket Chat Application </title>
    <link href="styles/bootstrap.min.css" rel="stylesheet">
    <link href="styles/style.css" rel="stylesheet">
    <script src="js/jquery-1.10.2.min.js"></script>
    <script src="js/main.js"></script>
</head>
<body>
<div id="main-content" class="container">
    <div class="row">
        <div class="col-md-8">
            <form class="form-inline">
                <div class="form-group">
                    <label for="connect">Chat Application:</label>
                    <button id="connect" type="button">Start New Chat</button>
                    <button id="disconnect" type="button" disabled="disabled">End Chat
                    </button>
                </div>
            </form>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12">
            <table id="chat">
                <thead>
                <tr>
                    <th>Welcome user. Please enter you name</th>
                </tr>
                </thead>
                <tbody id="helloworldmessage">
                </tbody>
            </table>
        </div>
        <div class="row">

            <div class="col-md-6">
                <form class="form-inline">
                    <div class="form-group">
                        <textarea id="user" placeholder="Write your message here..." required></textarea>
                    </div>
                    <button id="send" type="submit">Send</button>
                </form>
            </div>
        </div>
    </div>

</div>
</body>
</html>