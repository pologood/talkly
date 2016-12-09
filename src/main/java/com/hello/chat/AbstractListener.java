package com.hello.chat;

import com.corundumstudio.socketio.SocketIOServer;
import lombok.Getter;

/**
 * Created by lex on 2016/12/9.
 */
@Getter
public class AbstractListener {
    private SocketIOServer server;

    public AbstractListener(SocketIOServer server) {
        this.server = server;
    }
}
