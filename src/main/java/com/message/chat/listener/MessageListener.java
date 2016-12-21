package com.message.chat.listener;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.message.mq.Sender;
import com.message.service.CacheService;

import java.util.UUID;

/**
 * Created by lex on 2016/12/9.
 */
public class MessageListener
        extends AbstractListener
        implements DataListener<Message> {

    public MessageListener(
            SocketIOServer server,
            CacheService cache,
            Sender sender
    ) {
        super(server, cache, sender);
    }

    @Override
    public void onData(
            SocketIOClient client,
            Message message,
            AckRequest ackRequest
    ) throws Exception {
        message.setFrom(client.getSessionId().toString());
        getSender().chat(message);
    }
}
