package com.message.mq;

import com.message.chat.listener.LoginGuest;
import com.message.chat.listener.Message;
import com.message.chat.listener.Register;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by lex on 2016/12/20.
 */
@Service
public class Sender {

    private final static Logger log = LogManager.getLogger(Sender.class);
    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void loginAgent(Register register) {
        rabbitTemplate.convertAndSend("talkly.loginAgent", register);
    }

    public void loginGuest(LoginGuest message) {
        rabbitTemplate.convertAndSend("talkly.loginGuest", message);
    }

    public void logout(String clientId) {
        rabbitTemplate.convertAndSend("talkly.logout", clientId);
    }

    public void chat(Message message) {
        rabbitTemplate.convertAndSend("talkly.chat", message);
    }

    public void fetchLucky(LoginGuest guest) {
        rabbitTemplate.convertAndSend("talkly.guest.fetchLucky", guest);
    }
}
