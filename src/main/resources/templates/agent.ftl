<#include "/header.ftl">

<div id="chatVM" class="chat-container">
    <div class="people-list" id="people-list">
        <div class="search">
            <input type="text" placeholder="search"/>
            <i class="fa fa-search"></i>
        </div>
        <ul class="list">
            <li class="clearfix">
                <img class="avatar" src="img/anon-avatar.jpg" alt="avatar"/>
                <div class="about">
                    <div class="name">Vincent Porter</div>
                    <div class="status">
                        <i class="fa fa-circle online"></i> online
                    </div>
                </div>
            </li>

            <li class="clearfix">
                <img class="avatar" src="img/anon-avatar.jpg" alt="avatar"/>
                <div class="about">
                    <div class="name">Aiden Chavez</div>
                    <div class="status">
                        <i class="fa fa-circle offline"></i> left 7 mins ago
                    </div>
                </div>
            </li>

            <li class="clearfix">
                <img class="avatar" src="img/anon-avatar.jpg" alt="avatar"/>
                <div class="about">
                    <div class="name">Mike Thomas</div>
                    <div class="status">
                        <i class="fa fa-circle online"></i> online
                    </div>
                </div>
            </li>

            <li class="clearfix">
                <img class="avatar" src="img/anon-avatar.jpg" alt="avatar"/>
                <div class="about">
                    <div class="name">Erica Hughes</div>
                    <div class="status">
                        <i class="fa fa-circle online"></i> online
                    </div>
                </div>
            </li>

            <li class="clearfix">
                <img class="avatar" src="img/anon-avatar.jpg" alt="avatar"/>
                <div class="about">
                    <div class="name">Ginger Johnston</div>
                    <div class="status">
                        <i class="fa fa-circle online"></i> online
                    </div>
                </div>
            </li>

            <li class="clearfix">
                <img class="avatar" src="img/anon-avatar.jpg" alt="avatar"/>
                <div class="about">
                    <div class="name">Tracy Carpenter</div>
                    <div class="status">
                        <i class="fa fa-circle offline"></i> left 30 mins ago
                    </div>
                </div>
            </li>

            <li class="clearfix">
                <img class="avatar" src="img/anon-avatar.jpg" alt="avatar"/>
                <div class="about">
                    <div class="name">Christian Kelly</div>
                    <div class="status">
                        <i class="fa fa-circle offline"></i> left 10 hours ago
                    </div>
                </div>
            </li>

            <li class="clearfix">
                <img class="avatar" src="img/anon-avatar.jpg" alt="avatar"/>
                <div class="about">
                    <div class="name">Monica Ward</div>
                    <div class="status">
                        <i class="fa fa-circle online"></i> online
                    </div>
                </div>
            </li>

            <li class="clearfix">
                <img class="avatar" src="img/anon-avatar.jpg" alt="avatar"/>
                <div class="about">
                    <div class="name">Peyton Mckinney</div>
                    <div class="status">
                        <i class="fa fa-circle online"></i> online
                    </div>
                </div>
            </li>
        </ul>
    </div>
    <div class="chat">
        <div class="chat-history">
            <ul>
                <li class="clearfix">
                    <div class="message-data align-right">
                        <span class="message-data-time">10:10 AM, Today</span> &nbsp; &nbsp;
                        <span class="message-data-name">Olia</span> <i class="fa fa-circle me"></i>

                    </div>
                    <div class="message other-message float-right">
                        Hi Vincent, how are you? How is the project coming along?
                    </div>
                </li>
            </ul>
        </div> <!-- end chat-history -->

        <div class="chat-message clearfix">
            <textarea name="message-to-send" id="message-to-send" placeholder="Type your message" rows="3"></textarea>
            <i class="fa fa-file-o"></i> &nbsp;&nbsp;&nbsp;
            <i class="fa fa-file-image-o"></i>
            <button>Send</button>
        </div>
    </div>
</div>

<script src="https://cdn.socket.io/socket.io-1.4.5.js"></script>
<script src="http://cdn.jsdelivr.net/fingerprintjs2/1.4.1/fingerprint2.min.js"></script>
<script>
    var vm = new Vue({
        el: '#chatVM',
        data: {
            agents: []
        },
        methods: {
            init: function () {
                $.get('/api/agents', function (data) {
                    this.agents = data;
                })
            }
        }
    });
    vm.init();
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