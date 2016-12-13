package com.hello.service;

import com.hello.domain.User;
import com.hello.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


/**
 * Created by lex on 2016/12/13.
 */
@Service
public class AuthService implements AuthenticationProvider {
    @Autowired
    private UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        if (authentication.getPrincipal() == null
                || authentication.getCredentials() == null) {
            return null;
        }
        String username = String.valueOf(authentication.getPrincipal());
        String password = String.valueOf(authentication.getCredentials());
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("username not found" + username);
        }
        if (!password.equals(user.getPassword())) {
            throw new BadCredentialsException("password of "
                    + username + " is not " + user.getPassword());
        }
        Authentication auth = new UsernamePasswordAuthenticationToken(
                username,
                password,
                authentication.getAuthorities()
        );
        return auth;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
