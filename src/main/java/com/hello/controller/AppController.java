package com.hello.controller;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Created by lex on 2016/12/9.
 */
@Controller
public class AppController {
    @Autowired
    private SocketIOServer chatServer;

    @RequestMapping("/")
    public String index(Model model) {
        List<UUID> users = new ArrayList<>();
        Collection<SocketIOClient> clients = chatServer.getAllClients();
        for (SocketIOClient client : clients) {
            users.add(client.getSessionId());
        }
        model.addAttribute("users", users);
        return "index";
    }

    @RequestMapping("/guest")
    public String guest(Model model) {
        return "guest";
    }
}
