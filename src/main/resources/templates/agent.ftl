<#include "/header.ftl">
<h1>Hello Agent</h1>
<input id="to" type="text">
<input id="msg" type="text">
<button type="button" onClick="sendMessage()" class="btn" id="send">Send</button>
<button type="button" onClick="sendDisconnect()" class="btn">Disconnect</button>
<script src="https://cdn.socket.io/socket.io-1.4.5.js"></script>
<script src="http://cdn.jsdelivr.net/fingerprintjs2/1.4.1/fingerprint2.min.js"></script>
<script>
    function getURLParameter(name) {
        return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search) || [null, ''])[1].replace(/\+/g, '%20')) || null;
    }
    document.getElementById("to").value = getURLParameter('to');
    var socket = io.connect('http://localhost:9092');
    var fingerprint;
    new Fingerprint2().get(function (result, components) {
        fingerprint = result;
        socket.on('connect', function () {
            console.log('Client has connected to the server!');
            socket.emit('send_register', {
                username: '${username}',
                token: "zzzzz"
            });
        });
        socket.on('disconnect', function () {
            console.log('The client has disconnected!');
        });
        socket.on('get_message', function (data) {
            console.log(data);
        });
    });
    function sendDisconnect() {
        socket.disconnect();
    }
    function sendMessage() {
        socket.emit('send_message', {
            from: fingerprint,
            to: document.getElementById("to").value,
            content: document.getElementById("msg").value
        });
    }
</script>
<#include "/footer.ftl">