package com.message.service;

import com.message.chat.listener.LoginGuest;
import com.message.model.Guest;
import com.message.mq.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lex on 2016/12/27.
 */
@Service
public class QueueService {
    private final static int MAX_QUEUE_LEN = 1;
    private final static String LUCKY_SUFFIX = ":lucky";
    private final static String WAIT_SUFFIX = ":wait";
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private CacheService cache;
    @Autowired
    private Sender sender;

    public void push(String agentId, String guestId) {
        String luckyKey = agentId + LUCKY_SUFFIX;
        String waitKey = agentId + WAIT_SUFFIX;
        if (redisTemplate.opsForSet().size(luckyKey) <= MAX_QUEUE_LEN) {
            redisTemplate.opsForSet().add(luckyKey, guestId);
            sender.fetchLucky(new LoginGuest(agentId, guestId));
        } else {
            redisTemplate.opsForList().leftPush(waitKey, guestId);
        }
    }

    public void remove(String agentId, String guestId) {
        String luckyKey = agentId + LUCKY_SUFFIX;
        String waitKey = agentId + WAIT_SUFFIX;
        if (redisTemplate.opsForSet().isMember(luckyKey, guestId)) {
            redisTemplate.opsForSet().remove(luckyKey, guestId);
            String nextGuestId = redisTemplate.opsForList().rightPop(waitKey);
            if (nextGuestId != null) {
                redisTemplate.opsForSet().add(luckyKey, nextGuestId);
                sender.fetchLucky(new LoginGuest(agentId, guestId));
            }
        } else {
            redisTemplate.opsForList().remove(waitKey, 1, guestId);
        }
    }

    public List<Guest> getLuckyGuests(String agentId) {
        return getGuests(agentId, LUCKY_SUFFIX);
    }

    public List<Guest> getWaitGuests(String agentId) {
        return getGuests(agentId, WAIT_SUFFIX);
    }

    private List<Guest> getGuests(String agentId, String status) {
        Cursor<String> cursor = redisTemplate.opsForSet().scan(
                agentId + status,
                ScanOptions.NONE
        );
        List<Guest> guests = new ArrayList<>();
        while (cursor.hasNext()) {
            String fingerPrint = cursor.next();
            Guest guest = cache.getGuests().get(fingerPrint);
            if (guest != null) {
                guests.add(guest);
            }
        }
        return guests;
    }
}
