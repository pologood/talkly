package com.message.chat.listener;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.message.mq.Sender;
import com.message.service.CacheService;

/**
 * Created by lex on 2016/12/27.
 */
public class LoginGuestListener
        extends AbstractListener
        implements DataListener<LoginGuest> {

    public LoginGuestListener(
            SocketIOServer server,
            CacheService cache,
            Sender sender
    ) {
        super(server, cache, sender);
    }

    @Override
    public void onData(
            SocketIOClient client,
            LoginGuest message,
            AckRequest ackRequest
    ) throws Exception {
        message.setClientId(client.getSessionId().toString());
        getSender().loginGuest(message);
    }
}
