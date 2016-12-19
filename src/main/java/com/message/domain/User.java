package com.message.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by lex on 2016/12/13.
 */
@Getter
@Setter
@Entity(name = "talk_user")
public class User implements Serializable {
    @Id
    private Long id;
    private String email;                                 // 用户公司邮箱
    private String name;                                  // 用户真实姓名
    private String username;                              // 用户姓名
    private String nickname;                              // 用户显示昵称
    private String password;                              // 用户密码
    private Long onlineMinutes;                           // 用户在线时长,分钟
}
