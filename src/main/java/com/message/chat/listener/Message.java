package com.message.chat.listener;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by lex on 2016/12/9.
 */
@Getter
@Setter
public class Message implements Serializable {
    private String from;
    private String to;
    private String type;
    private String content;
    private Date createTime;

    public Message() {
    }

    public Message(String from, String to, String content, Date createTime) {
        this.from = from;
        this.to = to;
        this.content = content;
        this.createTime = createTime;
    }

}
