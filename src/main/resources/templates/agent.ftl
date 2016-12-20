<#include "/header.ftl">

<div id="chatVM" class="chat-container">
    <div class="people-list" id="people-list">
        <div class="search">
            <input type="text" placeholder="search"/>
            <i class="fa fa-search"></i>
        </div>
        <ul class="list">
            <li class="clearfix" v-for="agent in agents" v-on:click="selectAgent(agent)">
                <img class="avatar" src="img/anon-avatar.jpg" alt="avatar"/>
                <div class="about">
                    <div class="name">{{agent.name}}</div>
                </div>
            </li>
        </ul>
    </div>
    <div class="chat">
        <div class="chat-history">
            <ul>
                <li class="clearfix" v-for="msg in currentAgent.histories">
                <#--<div class="message-data align-right">-->
                        <#--<span class="message-data-time">{{msg.createTime}}</span> &nbsp; &nbsp;-->
                        <#--<span class="message-data-name">{{msg.from}}</span> <i class="fa fa-circle me"></i>-->

                    <#--</div>-->
                    <#--<div class="message float-right" v-bind:class="{'other-message': !msg.isMe, 'my-message':msg.isMe, 'float-right', msg.isMe}">-->
                        <#--{{msg.content}}-->
                    <#--</div>-->
                </li>

                <li>
                    <div class="message-data">
                        <span class="message-data-name"><i class="fa fa-circle online"></i> Vincent</span>
                        <span class="message-data-time">10:12 AM, Today</span>
                    </div>
                    <div class="message my-message">
                        Are we meeting today? Project has been already finished and I have results to show you.
                    </div>
                </li>

                <li class="clearfix">
                    <div class="message-data align-right">
                        <span class="message-data-time">10:14 AM, Today</span> &nbsp; &nbsp;
                        <span class="message-data-name">Olia</span> <i class="fa fa-circle me"></i>

                    </div>
                    <div class="message other-message float-right">
                        Well I am not sure. The rest of the team is not here yet. Maybe in an hour or so? Have you faced
                        any problems at the last phase of the project?
                    </div>
                </li>

                <li>
                    <div class="message-data">
                        <span class="message-data-name"><i class="fa fa-circle online"></i> Vincent</span>
                        <span class="message-data-time">10:20 AM, Today</span>
                    </div>
                    <div class="message my-message">
                        Actually everything was fine. I'm very excited to show this to our team.
                    </div>
                </li>

                <li>
                    <div class="message-data">
                        <span class="message-data-name"><i class="fa fa-circle online"></i> Vincent</span>
                        <span class="message-data-time">10:31 AM, Today</span>
                    </div>
                    <i class="fa fa-circle online"></i>
                    <i class="fa fa-circle online" style="color: #AED2A6"></i>
                    <i class="fa fa-circle online" style="color:#DAE9DA"></i>
                </li>

            </ul>
        </div> <!-- end chat-history -->

        <div class="chat-message clearfix">
            <textarea name="message-to-send" id="message-to-send"
                      v-model="message"
                      placeholder="Type your message" rows="3"></textarea>
            <i class="fa fa-file-o"></i> &nbsp;&nbsp;&nbsp;
            <i class="fa fa-file-image-o"></i>
            <button v-click="sendMessage(message)">Send</button>
        </div>
    </div>
</div>

<script src="https://cdn.socket.io/socket.io-1.4.5.js"></script>
<script src="http://cdn.jsdelivr.net/fingerprintjs2/1.4.1/fingerprint2.min.js"></script>
<script>
    var vm = new Vue({
        el: '#chatVM',
        data: {
            message: '',
            fingerPrint: '',
            currentAgent: {},
            agents: [{}]
        },
        methods: {
            init: function () {
                $.get('/api/agents', function (data) {
                    vm.agents = data;
                })
            },
            selectAgent: function (agent) {
                if (vm.currentAgent) {
                    vm.currentAgent.active = false;
                }
                vm.currentAgent = agent;
                agent.active = true;
            },
            sendMessage: function (message) {
                if (!message) return;
                var msg = {
                    from: vm.fingerPrint,
                    to: vm.currentAgent.username,
                    content: message,
                    type: 'text',
                    createTime: new Date(),
                    isMe: true
                };
                socket.emit('send_message', msg);
                vm.currentAgent.histories = vm.currentAgent.histories || [];
                vm.currentAgent.histories.push(msg)
            }
        }
    });
    var socket = io.connect('http://localhost:9092');
    new Fingerprint2().get(function (result, components) {
        vm.fingerPrint = result;
        socket.on('connect', function () {
            console.log('Client has connected to the server!');
            socket.emit('send_register', {
                username: '${username}',
                token: "zzzzz"
            }, function (data) {
                console.log('Client register is acked');
                vm.init();
            });
        });
        socket.on('disconnect', function () {
            console.log('The client has disconnected!');
        });
        socket.on('get_message', function (data) {
            console.log(data);
        });
    });
    function getURLParameter(name) {
        return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search) || [null, ''])[1].replace(/\+/g, '%20')) || null;
    }
    function sendDisconnect() {
        socket.disconnect();
    }
    function sendMessage() {
    }
</script>
<#include "/footer.ftl">