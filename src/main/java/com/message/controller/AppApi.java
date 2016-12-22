package com.message.controller;

import com.message.model.Agent;
import com.message.model.Guest;
import com.message.service.CacheService;
import com.message.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private UserService userService;

    @RequestMapping("/agents")
    public Collection<Agent> agents() {
        return userService.loadAgents();
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
