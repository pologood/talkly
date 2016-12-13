package com.hello.chat.msg;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by lex on 2016/12/13.
 */
@Getter
@Setter
public class Register implements Serializable {
    private String username;
    private String token;
    private String clientId;
}
