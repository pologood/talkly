<#include "/header.ftl">

<div id="chatVM" class="chat-container">
    <div class="people-list" id="people-list">
        <div class="search">
            <input type="text" placeholder="search" v-model="searchText"/>
            <i class="fa fa-search"></i>
        </div>
        <ul class="list">
            <li class="clearfix" v-for="agent in filteredAgents"
                v-on:click="selectAgent(agent)"
                v-bind:class="{'active':agent==currentAgent}">
                <img class="avatar" src="img/anon-avatar.jpg" alt="avatar"/>
                <div class="about">
                    <div class="name">{{agent.name}}</div>
                </div>
            </li>
        </ul>
    </div>
    <div class="chat" v-if="currentAgent.username">
        <div id="chat-history" class="chat-history">
            <ul>
                <li class="clearfix" v-for="msg in currentAgent.histories">
                    <div class="message-data" v-if="!msg.isMe">
                        <i class="fa fa-circle other"></i>
                        <span class="message-data-name">{{msg.from}}</span>&nbsp;&nbsp;
                        <span class="message-data-time">{{msg.createTime|moment}}</span>
                    </div>
                    <div class="message-data align-right" v-if="msg.isMe">
                        <span class="message-data-time">{{msg.createTime|moment}}</span>&nbsp;&nbsp;
                        <span class="message-data-name">Me</span>
                        <i class="fa fa-circle me"></i>
                    </div>
                    <div class="message"
                         v-bind:class="{'other-message': !msg.isMe, 'my-message':msg.isMe, 'float-right': msg.isMe}">
                        {{msg.content}}
                    </div>
                </li>
            </ul>
        </div>
        <div class="chat-message clearfix">
            <textarea name="message-to-send" id="message-to-send"
                      v-model="message"
                      placeholder="Type your message" rows="3"></textarea>
            <i class="fa fa-file-o"></i> &nbsp;&nbsp;&nbsp;
            <i class="fa fa-file-image-o"></i>
            <button v-on:click="sendMessage(message)">Send</button>
        </div>
    </div>
</div>

<script src="https://cdn.socket.io/socket.io-1.4.5.js"></script>
<script src="http://cdn.jsdelivr.net/fingerprintjs2/1.4.1/fingerprint2.min.js"></script>
<script>
    var vm = new Vue({
        el: '#chatVM',
        data: {
            searchText: '',
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
                vm.message = null;
                scrollHistoryToBottom();
            }
        },
        filters: {
            moment: function (date) {
                return moment(date).format('YYYY-MM-DD, HH:mm:ss');
            }
        },
        computed: {
            filteredAgents: function () {
                var self = this;
                if (!self.searchText) return self.agents;
                return self.agents.filter(function (agent) {
                    if (!agent.name) return false;
                    if (!agent.name.indexOf) return false;
                    return agent.name.indexOf(self.searchText) >= 0;
                })
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
            for (var i = 0; vm.agents && i < vm.agents.length; i++) {
                var agent = vm.agents[i];
                if (agent) {
                    agent.histories = agent.histories || [];
                    if (data.fingerPrint != vm.fingerPrint) {
                        data.createTime = new Date();
                        agent.histories.push(data);
                    }
                }
            }
            var agents = vm.agents;
            vm.agents = [];
            vm.agents = agents;
            console.log(data);
            scrollHistoryToBottom();
        });
    });
    function scrollHistoryToBottom() {
        setTimeout(function () {
            var objDiv = document.getElementById("chat-history");
            objDiv.scrollTop = objDiv.scrollHeight;
        })
    }
    function getURLParameter(name) {
        return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search) || [null, ''])[1].replace(/\+/g, '%20')) || null;
    }
    function sendDisconnect() {
        socket.disconnect();
    }
</script>
<#include "/footer.ftl">