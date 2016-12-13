package com.hello.repository;

import com.hello.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by lex on 2016/12/13.
 */
public interface UserRepository
        extends JpaRepository<User, Long> {
}
