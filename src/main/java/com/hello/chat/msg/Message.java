package com.hello.chat.msg;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Created by 浦云飞 on 2016/12/9.
 */
@Getter
@Setter
public class Message {
    private String from;
    private String to;
    private String type;
    private String content;

    public Message() {
    }

    public Message(String from, String to, String content) {
        this.from = from;
        this.to = to;
        this.content = content;
    }

}
