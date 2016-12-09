package com.hello.controller;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.transport.NamespaceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Created by lex on 2016/12/9.
 */
@RestController
@RequestMapping("/api")
public class AppApi {
    @Autowired
    private SocketIOServer chatServer;

    @RequestMapping("/users")
    public List<UUID> online() {
        List<UUID> users = new ArrayList<>();
        Collection<SocketIOClient> clients = chatServer.getAllClients();
        for (SocketIOClient client : clients) {
            users.add(client.getSessionId());
        }
        return users;
    }
}
