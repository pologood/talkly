package com.hello.chat;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by lex on 2016/12/9.
 */
@Getter
@Setter
public class Visitor {
    private String fingerPrint;
    private String sessionId;

    public Visitor(String fingerPrint, String sessionId) {
        this.fingerPrint = fingerPrint;
        this.sessionId = sessionId;
    }
}
