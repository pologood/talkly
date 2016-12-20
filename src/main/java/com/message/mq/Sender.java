package com.message.mq;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by lex on 2016/12/20.
 */
@Service
public class Sender {

    private final static Logger log = LogManager.getLogger(Sender.class);
    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send() {
        String context = "hello " + new Date();
        log.debug("Sender : " + context);
        this.rabbitTemplate.convertAndSend("talkly", context);
    }
}
