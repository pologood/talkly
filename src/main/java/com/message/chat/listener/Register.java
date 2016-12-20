package com.message.chat.listener;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by lex on 2016/12/13.
 */
@Getter
@Setter
public class Register implements Serializable {
    private String fingerPrint;
    private String username;
    private String token;
    private String clientId;

    @Override
    public String toString() {
        return "Register{" +
                "fingerPrint='" + fingerPrint + '\'' +
                ", username='" + username + '\'' +
                ", token='" + token + '\'' +
                ", clientId='" + clientId + '\'' +
                '}';
    }
}
