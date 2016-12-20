package com.message.chat.listener;

import com.corundumstudio.socketio.SocketIOServer;
import com.message.mq.Sender;
import com.message.service.CacheService;
import lombok.Getter;

/**
 * Created by lex on 2016/12/9.
 */
@Getter
public class AbstractListener {
    private SocketIOServer server;
    private CacheService cache;
    private Sender sender;

    public AbstractListener(
            SocketIOServer server,
            CacheService cache,
            Sender sender
    ) {
        this.server = server;
        this.cache = cache;
        this.sender = sender;
    }
}
