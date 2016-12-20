package com.message.chat.listener;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.message.mq.Sender;
import com.message.service.CacheService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by lex on 2016/12/13.
 */
@Component
public class RegisterListener
        extends AbstractListener
        implements DataListener<Register> {

    private final static Logger log = LogManager.getLogger(RegisterListener.class);

    public RegisterListener(
            SocketIOServer server,
            CacheService cache,
            Sender sender
    ) {
        super(server, cache, sender);
    }

    @Override
    public void onData(
            SocketIOClient client,
            Register message,
            AckRequest ackRequest
    ) throws Exception {
        message.setClientId(client.getSessionId().toString());
        getSender().loginAgent(message);
    }
}
