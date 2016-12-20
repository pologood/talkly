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
}
