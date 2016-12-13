package com.hello.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

/**
 * Created by lex on 2016/12/13.
 */
@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void put(String key, Object value) {
        ValueOperations<String, Object> valueops = redisTemplate.opsForValue();
        valueops.set(key, value);
    }

    public void get(String key) {
        ValueOperations<String, Object> valueops = redisTemplate.opsForValue();
        valueops.get(key);
    }

    public void remove(String key) {
        redisTemplate.delete(key);
    }
}
