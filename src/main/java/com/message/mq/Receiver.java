package com.message.mq;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Created by lex on 2016/12/20.
 */
@Component
@RabbitListener(queues = "talkly")
public class Receiver {
    private final static Logger log = LogManager.getLogger(Receiver.class);

    @RabbitHandler
    public void process(String hello) {
        log.debug("Receiver : " + hello);
    }
}
