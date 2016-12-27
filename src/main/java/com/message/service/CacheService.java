package com.message.service;

import com.message.model.Agent;
import com.message.model.Guest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lex on 2016/12/13.
 */
@Service
public class CacheService {
    private Map<String, Agent> agents = new ConcurrentHashMap<>();
    private Map<String, String> clients = new ConcurrentHashMap<>();
    private Map<String, Guest> guests = new ConcurrentHashMap<>();

    @Autowired
    private Jedis redisTemplate;
    @Autowired
    private SerializerService serializerService;

    public void put(String key, Object value) {
        redisTemplate.set(
                serializerService.serialize(key),
                serializerService.serialize(value)
        );
    }

    public Object get(String key) {
        byte[] result = redisTemplate.get(serializerService.serialize(key));
        return result == null ? null : serializerService.deserialize(result);
    }

    public void remove(String key) {
        redisTemplate.del(serializerService.serialize(key));
    }

    public Map<String, String> getClients() {
        return clients;
    }

    public Map<String, Agent> getAgents() {
        return agents;
    }

    public Map<String, Guest> getGuests() {
        return guests;
    }
}
