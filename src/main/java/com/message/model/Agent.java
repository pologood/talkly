package com.message.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by lex on 2016/12/13.
 */
@Getter
@Setter
public class Agent implements Serializable {
    private String name;
    private String clientId;

    public Agent(String name, String clientId) {
        this.name = name;
        this.clientId = clientId;
    }
}
