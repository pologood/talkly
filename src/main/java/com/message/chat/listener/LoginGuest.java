package com.message.chat.listener;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by lex on 2016/12/27.
 */
@Getter
@Setter
public class LoginGuest implements Serializable {
    private String fingerPrint;
    private String agentId;

    @Override
    public String toString() {
        return "LoginGuest{" +
                "fingerPrint='" + fingerPrint + '\'' +
                ", agentId='" + agentId + '\'' +
                '}';
    }
}
