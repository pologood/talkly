package com.message.chat.listener;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.message.service.CacheService;

import java.util.UUID;

/**
 * Created by lex on 2016/12/9.
 */
public class MessageListener
        extends AbstractListener
        implements DataListener<Message> {

    public MessageListener(SocketIOServer server, CacheService cache) {
        super(server, cache);
    }

    @Override
    public void onData(
            SocketIOClient client,
            Message message,
            AckRequest ackRequest
    ) throws Exception {
        getServer().getClient(UUID.fromString(message.getTo()))
                .sendEvent("get_message", message);
    }
}
