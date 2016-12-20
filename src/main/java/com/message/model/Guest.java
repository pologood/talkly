package com.message.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by lex on 2016/12/20.
 */
@Getter
@Setter
public class Guest {
    private String fingerPrint;
    private String name;
    private String clientId;

    public Guest(String fingerPrint, String name, String clientId) {
        this.fingerPrint = fingerPrint;
        this.name = name;
        this.clientId = clientId;
    }
}
