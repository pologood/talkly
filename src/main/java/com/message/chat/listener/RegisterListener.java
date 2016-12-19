package com.message.chat.listener;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.message.service.CacheService;
import org.springframework.stereotype.Component;

/**
 * Created by lex on 2016/12/13.
 */
@Component
public class RegisterListener
        extends AbstractListener
        implements DataListener<Register> {

    public RegisterListener(SocketIOServer server, CacheService cache) {
        super(server, cache);
    }

    @Override
    public void onData(
            SocketIOClient client,
            Register message,
            AckRequest ackRequest
    ) throws Exception {
        client.set("agent", message.getUsername());
        message.setClientId(client.getSessionId().toString());
        getCache().put(message.getUsername(), message);
    }
}
