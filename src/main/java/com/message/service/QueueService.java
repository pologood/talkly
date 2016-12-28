package com.message.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by lex on 2016/12/27.
 */
@Service
public class QueueService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void push(String agentId, String guestId) {
        String luckyKey = agentId + ":lucky";
        String waitKey = agentId + ":wait";
        if (redisTemplate.opsForSet().size(luckyKey) <= 5) {
            redisTemplate.opsForSet().add(luckyKey, guestId);
        } else {
            redisTemplate.opsForList().rightPush(waitKey, guestId);
        }
    }

    public void remove(String agentId, String guestId) {
        String luckyKey = agentId + ":lucky";
        String waitKey = agentId + ":wait";
        if (redisTemplate.opsForSet().isMember(luckyKey, guestId)) {
            redisTemplate.opsForSet().remove(luckyKey, guestId);
            String nextGuestId = redisTemplate.opsForList().leftPop(waitKey);
            if (nextGuestId != null) {
                redisTemplate.opsForSet().add(luckyKey, nextGuestId);
            }
        } else {
            redisTemplate.opsForList().remove(waitKey, 1, guestId);
        }
    }
}
