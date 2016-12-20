package com.message.chat.listener;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.message.mq.Sender;
import com.message.service.CacheService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Created by lex on 2016/12/15.
 */
public class DisconnectListener
        extends AbstractListener
        implements com.corundumstudio.socketio.listener.DisconnectListener {

    private final static Logger log = LogManager.getLogger(DisconnectListener.class);

    public DisconnectListener(
            SocketIOServer server,
            CacheService cache,
            Sender sender
    ) {
        super(server, cache, sender);
    }

    @Override
    public void onDisconnect(SocketIOClient client) {
        getSender().logout(client.getSessionId().toString());
    }
}
