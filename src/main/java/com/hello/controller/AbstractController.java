package com.hello.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by lex on 2016/12/13.
 */
public abstract class AbstractController {
    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() != null) {
            return String.valueOf(auth.getPrincipal());
        }
        return null;
    }
}
