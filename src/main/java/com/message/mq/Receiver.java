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

import java.util.ArrayList;
import java.util.List;
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
            cache.getClients().put(
                    message.getClientId().toString(),
                    message.getUsername()
            );
            server.getBroadcastOperations().sendEvent(
                    "update_agents",
                    cache.getAgents().values()
            );
            if (cache.get(message.getUsername() + ":offline") != null) {
                server.getClient(UUID.fromString(message.getClientId())).sendEvent(
                        "get_messages",
                        cache.get(message.getUsername() + ":offline")
                );
                cache.remove(message.getUsername() + ":offline");
            }
        } else {
            cache.getGuests().put(message.getFingerPrint(), new Guest(
                    message.getFingerPrint(),
                    message.getFingerPrint(),
                    message.getClientId()
            ));
            cache.getClients().put(
                    message.getClientId().toString(),
                    message.getFingerPrint()
            );
        }
    }

    @RabbitListener(queues = "talkly.logout")
    @RabbitHandler
    public void logout(String clientId) {
        if (cache.getClients().containsKey(clientId)) {
            String username = cache.getClients().get(clientId);
            cache.getClients().remove(clientId);
            if (cache.getAgents().containsKey(username)) {
                server.getBroadcastOperations().sendEvent(
                        "update_agents",
                        cache.getAgents().keySet()
                );
                cache.getAgents().remove(username);
            }
            if (cache.getGuests().containsKey(username)) {
                cache.getGuests().remove(username);
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

        String fromUsername = cache.getClients().get(message.getFrom());
        if (fromUsername == null) {
            log.error("fromUsername not found");
            return;
        }

        if (toClientId != null) {
            /**
             * user online
             */
            server.getClient(UUID.fromString(toClientId)).sendEvent(
                    "get_message",
                    new Message(
                            fromUsername,
                            message.getTo(),
                            message.getContent(),
                            message.getCreateTime()
                    )
            );
        } else {
            /**
             * user offline
             */
            List<Message> offlineMessages = null;
            if (cache.get(message.getTo() + ":offline") == null) {
                offlineMessages = new ArrayList<>();
            } else {
                offlineMessages = (List<Message>) cache.get(message.getTo() + ":offline");
            }
            offlineMessages.add(new Message(
                    fromUsername,
                    message.getTo(),
                    message.getContent(),
                    message.getCreateTime()
            ));
            cache.put(message.getTo() + ":offline", offlineMessages);
        }
    }
}
