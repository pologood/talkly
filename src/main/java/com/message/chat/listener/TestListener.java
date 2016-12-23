package com.message.chat.listener;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;

/**
 * Created by lex on 2016/12/23.
 */
public class TestListener
        implements DataListener<String> {
    @Override
    public void onData(
            SocketIOClient client,
            String s,
            AckRequest ackRequest
    ) throws Exception {
        client.sendEvent("test", s);
    }
}
