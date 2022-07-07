package org.barren.core.auth.utils;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用于解析jwt用户信息
 *
 * @author cxs
 **/
@Data
public class UserInfo implements Serializable {

    private Long id;    //用户ID
    private String username;  //用户名字
    private String phone;  //用户电话号码
    private String nickname;  //用户昵称
    private List<String> roles; // 用户角色
    private List<String> authorities; // 用户权限
    private List<String> permissions; // 用户权限
}
