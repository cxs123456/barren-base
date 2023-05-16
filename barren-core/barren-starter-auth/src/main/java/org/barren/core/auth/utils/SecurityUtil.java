package org.barren.core.auth.utils;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;

/**
 * 获取当前登录的用户
 *
 * @author cxs
 */
@Slf4j
public class SecurityUtil {

    /**
     * 获取当前登录的用户
     *
     * @return UserDetails
     */
    public static UserInfo getCurrentUser() {
        JSONObject json = AuthUtil.getUserInfo();
        UserInfo userInfo = json.toJavaObject(UserInfo.class);
        return userInfo;
    }

}
