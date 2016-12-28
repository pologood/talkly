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
    private String clientId;

    public LoginGuest() {
    }

    public LoginGuest(String agentId, String fingerPrint) {
        this.agentId = agentId;
        this.fingerPrint = fingerPrint;
    }

    @Override
    public String toString() {
        return "LoginGuest{" +
                "fingerPrint='" + fingerPrint + '\'' +
                ", agentId='" + agentId + '\'' +
                ", clientId='" + clientId + '\'' +
                '}';
    }
}
