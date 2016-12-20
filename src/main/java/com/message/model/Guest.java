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

    public Guest(String fingerPrint) {
        this.fingerPrint = fingerPrint;
    }
}
