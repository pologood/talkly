package com.message.controller;

import com.message.model.Agent;
import com.message.model.Guest;
import com.message.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created by lex on 2016/12/9.
 */
@RestController
@RequestMapping("/api")
public class AppApi {
    @Autowired
    private CacheService cache;

    @RequestMapping("/agents")
    public List<Agent> agents() {
        List<Agent> agents = new ArrayList<>();
        for (Map.Entry<String, String> entry : cache.getAgents().entrySet()) {
            agents.add(new Agent(entry.getKey(), entry.getValue()));
        }
        return agents;
    }

    @RequestMapping("/guests")
    public List<Guest> guests() {
        List<Guest> guests = new ArrayList<>();
        for (Map.Entry<String, String> entry : cache.getGuests().entrySet()) {
            guests.add(new Guest(entry.getKey()));
        }
        return guests;
    }
}
