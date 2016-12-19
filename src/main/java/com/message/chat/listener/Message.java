package com.message.chat.listener;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by lex on 2016/12/9.
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
