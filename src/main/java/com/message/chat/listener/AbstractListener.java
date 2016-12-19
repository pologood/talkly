package com.message.chat.listener;

import com.corundumstudio.socketio.SocketIOServer;
import com.message.service.CacheService;
import lombok.Getter;

/**
 * Created by lex on 2016/12/9.
 */
@Getter
public class AbstractListener {
    private SocketIOServer server;
    private CacheService cache;

    public AbstractListener(SocketIOServer server, CacheService cache) {
        this.server = server;
        this.cache = cache;
    }
}