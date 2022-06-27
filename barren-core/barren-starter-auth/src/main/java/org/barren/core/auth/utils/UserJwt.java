package org.barren.core.auth.utils;


import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

/**
 * jwt 中存储的额外用户信息
 */
@Getter
@Setter
public class UserJwt extends User {
    private Long id;    //用户ID
    private String phone;  //用户电话号码
    private String nickname;  //用户昵称
    private List<String> roles;

    public UserJwt(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

}
