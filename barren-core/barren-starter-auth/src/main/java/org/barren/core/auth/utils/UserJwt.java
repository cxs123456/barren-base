package org.barren.core.auth.utils;


import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@Setter
public class UserJwt extends User {
    private String id;    //用户ID
    private String name;  //用户名字
    private String phone;  //用户电话号码
    private String nickname;  //用户电话号码

    public UserJwt(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

}
