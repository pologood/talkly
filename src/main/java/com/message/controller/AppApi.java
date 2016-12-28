package com.message.controller;

import com.message.model.Agent;
import com.message.model.Guest;
import com.message.service.CacheService;
import com.message.service.QueueService;
import com.message.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created by lex on 2016/12/9.
 */
@RestController
@RequestMapping("/api")
public class AppApi extends AbstractController {
    @Autowired
    private CacheService cache;
    @Autowired
    private QueueService queueService;
    @Autowired
    private UserService userService;

    @RequestMapping("/agents")
    public Collection<Agent> agents() {
        return userService.loadAgents();
    }

    @RequestMapping("/agent/guests/lucky/{:agentId}")
    public List<Guest> getLuckyGuests(
            @PathVariable("agentId") String agentId
    ) {
        return queueService.getLuckyGuests(agentId);
    }

    @RequestMapping("/agent/guests/wait/{:agentId}")
    public List<Guest> getWaitGuests(
            @PathVariable("agentId") String agentId
    ) {
        return queueService.getWaitGuests(agentId);
    }

    @RequestMapping("/guests")
    public Collection<Guest> guests() {
        return cache.getGuests().values();
    }

    @RequestMapping("/messages/offline/get")
    public Object getOfflineMessages() {
        return cache.get(getCurrentUsername() + ":offline");
    }

    @RequestMapping(value = "/messages/offline/clear", method = RequestMethod.POST)
    public String clearOfflineMessages() {
        cache.remove(getCurrentUsername() + ":offline");
        return "success";
    }
}
