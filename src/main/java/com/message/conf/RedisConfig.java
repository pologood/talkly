package com.message.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

/**
 * Created by lex on 2016/12/13.
 */
@Configuration
public class RedisConfig {
    @Bean
    public Jedis redisTemplate(
            @Value("${spring.redis.host}") String host,
            @Value("${spring.redis.port}") int port
    ) {
        Jedis jedis = new Jedis(host, port);
        return jedis;
    }
}
