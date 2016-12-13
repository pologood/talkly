package com.hello.controller;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.hello.model.Agent;
import com.hello.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Created by lex on 2016/12/9.
 */
@Controller
public class AppController extends AbstractController {
    @Autowired
    private SocketIOServer chatServer;
    @Autowired
    private CacheService cache;

    @RequestMapping("/")
    public String index(Model model) {
        List<Agent> users = new ArrayList<>();
        Collection<SocketIOClient> clients = chatServer.getAllClients();
        for (SocketIOClient client : clients) {
            users.add(new Agent(
                    client.get("agent") != null ? client.get("agent") : null,
                    client.getSessionId().toString())
            );
        }
        model.addAttribute("users", users);
        return "index";
    }

    @RequestMapping("/guest")
    public String guest(Model model) {
        return "guest";
    }

    @RequestMapping("/agent")
    public String agent(Model model) {
        model.addAttribute("username", getCurrentUsername());
        return "agent";
    }

    @RequestMapping("/login")
    public String login(
            @RequestParam(name = "username", required = false) String username,
            @RequestParam(name = "password", required = false) String password
    ) {
        return "login";
    }
}
