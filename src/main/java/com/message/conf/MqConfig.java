package com.message.conf;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by lex on 2016/12/20.
 */
@Configuration
public class MqConfig {

    @Bean
    public Queue talklyQueue() {
        return new Queue("talkly");
    }

    @Bean
    public Queue loginAgentQueue() {
        return new Queue("talkly.loginAgent");
    }

    @Bean
    public Queue logoutQueue() {
        return new Queue("talkly.logout");
    }

    @Bean
    public Queue chatQueue() {
        return new Queue("talkly.chat");
    }
}
