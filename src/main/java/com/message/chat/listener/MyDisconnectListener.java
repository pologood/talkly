package com.message.chat.listener;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.message.mq.Sender;
import com.message.service.CacheService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Created by lex on 2016/12/15.
 */
public class MyDisconnectListener
        extends AbstractListener
        implements DisconnectListener {

    private final static Logger log = LogManager.getLogger(MyDisconnectListener.class);

    public MyDisconnectListener(
            SocketIOServer server,
            CacheService cache,
            Sender sender
    ) {
        super(server, cache, sender);
    }

    @Override
    public void onDisconnect(SocketIOClient socketIOClient) {
        log.debug("Disconnect clientId = " + socketIOClient.getSessionId());
    }
}
