package com.message.chat.listener;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.message.service.CacheService;

/**
 * Created by lex on 2016/12/15.
 */
public class MyDisconnectListener
        extends AbstractListener
        implements DisconnectListener {

    public MyDisconnectListener(SocketIOServer server, CacheService cache) {
        super(server, cache);
    }

    @Override
    public void onDisconnect(SocketIOClient socketIOClient) {

    }
}
