package com.hello.chat.msg;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.hello.chat.AbstractListener;
import com.hello.chat.Visitor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by 浦云飞 on 2016/12/9.
 */
public class MessageListener
        extends AbstractListener
        implements DataListener<Message> {

    public MessageListener(SocketIOServer server) {
        super(server);
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
