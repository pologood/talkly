package com.message.repository;

import com.message.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by lex on 2016/12/13.
 */
@Repository
public interface UserRepository
        extends JpaRepository<User, Long> {
    User findByEmail(String email);

    User findByUsername(String username);
}
