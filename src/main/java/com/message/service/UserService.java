package com.message.service;

import com.message.domain.User;
import com.message.model.Agent;
import com.message.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by lex on 2016/12/22.
 */
@Service
public class UserService {
    @Autowired
    private CacheService cache;
    @Autowired
    private UserRepository userRepo;

    public List<Agent> loadAgents() {
        Map<String, Agent> onlineAgents = cache.getAgents();
        List<Agent> result = null;
        if (!onlineAgents.isEmpty()) {
            result = new ArrayList<>(onlineAgents.values());
        } else {
            result = new ArrayList<>();
        }
        List<User> users = userRepo.findAll();
        for (User user : users) {
            if (!onlineAgents.containsKey(user.getUsername())) {
                Agent agent = new Agent(
                        user.getUsername(),
                        user.getName(),
                        null
                );
                agent.setOnline(false);
                result.add(agent);
            }
        }
        return result;
    }
}
