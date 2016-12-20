package com.message.mq;

import com.corundumstudio.socketio.SocketIOServer;
import com.message.chat.listener.Message;
import com.message.chat.listener.Register;
import com.message.domain.User;
import com.message.model.Agent;
import com.message.model.Guest;
import com.message.repository.UserRepository;
import com.message.service.CacheService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;
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
    @Autowired
    private UserRepository userRepo;

    @RabbitListener(queues = "talkly.loginAgent")
    @RabbitHandler
    public void loginAgent(Register message) {
        log.debug("Receiver : " + message.toString());
        if (message.getUsername() != null) {
            User user = userRepo.findByUsername(message.getUsername());
            if (user == null) {
                return;
            }
            cache.getAgents().put(message.getUsername(), new Agent(
                    message.getUsername(),
                    user.getName(),
                    message.getClientId().toString()
            ));
            server.getBroadcastOperations().sendEvent(
                    "update_agents",
                    cache.getAgents().values()
            );
        } else {
            cache.getGuests().put(message.getFingerPrint(), new Guest(
                    message.getFingerPrint(),
                    message.getFingerPrint(),
                    message.getClientId()
            ));
        }
    }

    @RabbitListener(queues = "talkly.logout")
    @RabbitHandler
    public void logout(String clientId) {
        for (Map.Entry<String, Agent> entry : cache.getAgents().entrySet()) {
            if (entry.getValue() != null
                    && clientId.equals(entry.getValue().getClientId())) {
                cache.getAgents().remove(entry.getKey());
                server.getBroadcastOperations().sendEvent(
                        "update_agents",
                        cache.getAgents().keySet()
                );
                break;
            }
        }
        for (Map.Entry<String, Guest> entry : cache.getGuests().entrySet()) {
            if (entry.getValue() != null
                    && clientId.equals(entry.getValue().getClientId())) {
                cache.getGuests().remove(entry.getKey());
                break;
            }
        }
    }

    @RabbitListener(queues = "talkly.chat")
    @RabbitHandler
    public void chat(Message message) {
        String toClientId = null;
        if (cache.getAgents().containsKey(message.getTo())) {
            toClientId = cache.getAgents().get(message.getTo()).getClientId();
        }
        if (cache.getGuests().containsKey(message.getTo())) {
            toClientId = cache.getGuests().get(message.getTo()).getClientId();
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
