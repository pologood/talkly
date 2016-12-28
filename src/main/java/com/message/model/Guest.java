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
    private String agentId;
    private String clientId;

    public Guest(
            String fingerPrint,
            String name,
            String clientId,
            String agentId
    ) {
        this.fingerPrint = fingerPrint;
        this.name = name;
        this.clientId = clientId;
        this.agentId = agentId;
    }
}
