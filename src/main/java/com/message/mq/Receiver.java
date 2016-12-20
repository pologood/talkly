package com.message.mq;

import com.corundumstudio.socketio.SocketIOServer;
import com.message.chat.listener.Message;
import com.message.chat.listener.Register;
import com.message.service.CacheService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Created by lex on 2016/12/20.
 */
@Component
public class Receiver {
    private final static Logger log = LogManager.getLogger(Receiver.class);
    @Autowired
    private CacheService cache;
    @Autowired
    @Qualifier("chatServer")
    private SocketIOServer server;

    @RabbitListener(queues = "talkly.loginAgent")
    @RabbitHandler
    public void loginAgent(Register message) {
        log.debug("Receiver : " + message.toString());
        if (message.getUsername() != null) {
            cache.getAgents().put(message.getUsername(), message.getClientId());
            server.getBroadcastOperations().sendEvent(
                    "update_agents",
                    cache.getAgents().keySet()
            );
        } else {
            cache.getGuests().put(message.getFingerPrint(), message.getClientId());
        }
    }

    @RabbitListener(queues = "talkly.logout")
    @RabbitHandler
    public void logout(String clientId) {
        if (cache.getAgents().containsValue(clientId)) {
            cache.getAgents().values().remove(clientId);
            server.getBroadcastOperations().sendEvent(
                    "update_agents",
                    cache.getAgents().keySet()
            );
        }
        if (cache.getGuests().containsValue(clientId)) {
            cache.getGuests().values().remove(clientId);
        }
    }

    @RabbitListener(queues = "talkly.chat")
    @RabbitHandler
    public void chat(Message message) {
        String toClientId = null;
        if (cache.getAgents().containsKey(message.getTo())) {
            toClientId = cache.getAgents().get(toClientId);
        }
        if (cache.getGuests().containsKey(message.getTo())) {
            toClientId = cache.getGuests().get(toClientId);
        }
        if (toClientId != null) {
            server.getClient(UUID.fromString(toClientId)).sendEvent(
                    "get_message",
                    new Message(
                            message.getTo(),
                            message.getFrom(),
                            message.getContent()
                    )
            );
        }
    }
}
