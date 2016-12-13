package com.hello.conf;

import com.corundumstudio.socketio.SocketIOServer;
import com.hello.chat.msg.Message;
import com.hello.chat.msg.MessageListener;
import com.hello.chat.msg.Register;
import com.hello.chat.msg.RegisterListener;
import com.hello.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by lex on 2016/12/9.
 */
@Configuration
public class ChatServerConfig {

    @Autowired
    private CacheService cache;

    @Bean
    public SocketIOServer chatServer(
            @Value("${socket.port}") Integer socketPort
    ) {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();

        config.setPort(socketPort);

        SocketIOServer server = new SocketIOServer(config);

        server.addEventListener("send_message",
                Message.class,
                new MessageListener(server, cache)
        );
        server.addEventListener("send_register",
                Register.class,
                new RegisterListener(server, cache)
        );

        server.start();
        return server;
    }
}
