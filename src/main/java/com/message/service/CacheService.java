package com.message.service;

import com.message.model.Agent;
import com.message.model.Guest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lex on 2016/12/13.
 */
@Service
public class CacheService {
    private Map<String, Agent> agents = new HashMap<>();
    private Map<String, Guest> guests = new HashMap<>();

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void put(String key, Object value) {
        ValueOperations<String, Object> valueops = redisTemplate.opsForValue();
        valueops.set(key, value);
    }

    public Object get(String key) {
        ValueOperations<String, Object> valueops = redisTemplate.opsForValue();
        return valueops.get(key);
    }

    public void remove(String key) {
        redisTemplate.delete(key);
    }

    public Map<String, Agent> getAgents() {
        return agents;
    }

    public Map<String, Guest> getGuests() {
        return guests;
    }
}
