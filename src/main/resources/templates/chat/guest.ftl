<#include "/chat/header.ftl">
<h1>Hello Guest</h1>
<div id="guestVM">
    <div v-for="agent in agents">
        <a href="javascript:void()"
           v-on:click="selectAgent(agent)">{{agent.name}}</a>
        <span>{{agent.online?'在线':'离线'}}</span>
    </div>
</div>
<script>
    var vm = new Vue({
        el: '#guestVM',
        data: {
            agents: []
        },
        methods: {
            init: function () {
                $.get('/api/agents', function (data) {
                    vm.agents = data;
                })
            },
            selectAgent: function (agent) {
                socket.emit('send_login', {
                    fingerPrint: fingerprint,
                    agentId: agent.username
                });
            }
        }
    });
    vm.init();
    var socket = io.connect('http://localhost:9092');
    var fingerprint;
    new Fingerprint2().get(function (result, components) {
        fingerprint = result;
        socket.on('connect', function () {
            console.log('Client has connected to the server!');
        });
        socket.on('disconnect', function () {
            console.log('The client has disconnected!');
        });
        socket.on('get_message', function (data) {
            console.log(data);
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
    });
</script>
<#include "/chat/footer.ftl">