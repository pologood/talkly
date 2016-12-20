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
    private String username;
    private String name;

    public Agent(String username, String name) {
        this.username = username;
        this.name = name;
    }
}
