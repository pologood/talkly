<#include "/chat/header.ftl">

<div id="chatVM" class="chat-container">
    <div class="people-list" id="people-list">
        <div class="search">
            <input type="text" placeholder="搜索" v-model="searchText"/>
            <i class="fa fa-search"></i>
        </div>
        <ul class="list">
            <li class="clearfix" v-for="agent in filteredAgents"
                v-on:click="selectAgent(agent)"
                v-bind:class="{'active':agent==currentAgent}">
                <img class="avatar" src="chat/img/anon-avatar.jpg" alt="avatar"
                     v-bind:class="{offline:!agent.online}"/>
                <div class="about">
                    <div class="name">{{agent.name}}
                        <i class="fa fa-circle text-danger blink" v-if="agent.hasNewMsg"></i>
                    </div>
                </div>
            </li>
            <li class="clearfix" v-for="guest in guests"
                v-on:click="selectAgent(guest)"
                v-bind:class="{'active':guest==currentAgent}">
                <img class="avatar" src="chat/img/anon-avatar.jpg" alt="avatar"/>
                <div class="about">
                    <div class="name">{{guest.name}}
                        <i class="fa fa-circle text-danger blink" v-if="guest.hasNewMsg"></i>
                    </div>
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
                        <span class="message-data-name">我</span>
                        <i class="fa fa-circle me"></i>
                    </div>
                    <div class="message"
                         v-bind:class="{'other-message': !msg.isMe, 'my-message':msg.isMe, 'float-right': msg.isMe}">
                        <pre v-html="msg.content"></pre>
                    </div>
                </li>
            </ul>
        </div>
        <div class="chat-message clearfix">
            <textarea name="message-to-send" id="message-to-send"
                      v-model="message"
                      placeholder="输入文字..." rows="3"></textarea>
            <i class="fa fa-file-o"></i> &nbsp;&nbsp;&nbsp;
            <i class="fa fa-file-image-o"></i>
            <button v-on:click="sendMessage(message)">发送</button>
        </div>
    </div>
</div>

<script>
    var vm = new Vue({
        el: '#chatVM',
        data: {
            searchText: '',
            message: '',
            fingerPrint: '',
            currentAgent: {},
            agents: [],
            guests: []
        },
        methods: {
            init: function () {
                $.get('/api/agents', function (data) {
                    vm.agents = data;
                })
                loadGuests();
            },
            selectAgent: function (agent) {
                if (vm.currentAgent) {
                    vm.currentAgent.active = false;
                }
                vm.currentAgent = agent;
                agent.active = true;
                if (agent.hasNewMsg) {
                    agent.hasNewMsg = false;
                    $.post('/api/messages/offline/clear', function (data) {
                    });
                }
                scrollHistoryToBottom();
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

                socket.on('get_messages', function (data) {
                    var used;
                    interval(function () {
                        if (used) return;
                        var agents = vm.agents;
                        for (var i = 0; agents && i < agents.length; i++) {
                            var agent = agents[i];
                            if (agent) {
                                agent.histories = agent.histories || [];
                                for (var j = 0; data && j < data.length; j++) {
                                    var msg = data[j];
                                    if (msg && msg.from == agent.username) {
                                        agent.histories.push(msg);
                                        if (agent != vm.currentAgent) {
                                            agent.hasNewMsg = true;
                                            used = 1;
                                        }
                                    }
                                }
                            }
                        }
                        vm.agents = [];
                        vm.agents = agents;
                    }, 500, 10)
                });
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
                    if (data.from == agent.username) {
                        agent.histories.push(data);
                        if (agent != vm.currentAgent) {
                            agent.hasNewMsg = true;
                        }
                    }
                }
            }
            var agents = vm.agents;
            vm.agents = [];
            vm.agents = agents;
            console.log(data);
            newExcitingAlerts("!!!您收到一条新消息!!!");
            scrollHistoryToBottom();
        });
        socket.on('update_agents', function (data) {
            var onlineAgents = [];
            var offlineAgents = [];
            for (var i = 0; vm.agents && i < vm.agents.length; i++) {
                var agent = vm.agents[i];
                if (agent) {
                    if (data.indexOf(agent.username) >= 0) {
                        agent.online = true;
                        onlineAgents.push(agent);
                    } else {
                        agent.online = false;
                        offlineAgents.push(agent);
                    }
                }
            }
            vm.agents = onlineAgents.concat(offlineAgents);
        });
        socket.on('update_guests', function (data) {
            loadGuests();
        });
    });
    function loadGuests() {
        $.get('/api/agent/guests/lucky/${username}', function (data) {
            for (var i = 0; data && i < data.length; i++) {
                var guest = data[i];
                if (guest) {
                    guest.name = '游客' + guest.name.substring(0, 4);
                }
            }
            vm.guests = data;
        })
    }
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
    function newExcitingAlerts(msg) {
        $.titleAlert(msg, {
            requireBlur: true,
            stopOnFocus: true,
            duration: 0,
            interval: 700
        });
    }
    function interval(func, wait, times) {
        var interv = function (w, t) {
            return function () {
                if (typeof t === "undefined" || t-- > 0) {
                    setTimeout(interv, w);
                    try {
                        func.call(null);
                    }
                    catch (e) {
                        t = 0;
                        throw e.toString();
                    }
                }
            };
        }(wait, times);

        setTimeout(interv, wait);
        return interv;
    }
</script>
<#include "/chat/footer.ftl">