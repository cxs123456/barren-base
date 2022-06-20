package org.barren.config;

import org.apache.commons.lang3.StringUtils;
import org.barren.core.auth.utils.UserJwt;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/***
 * 自定义用户认证详情
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    // @Autowired
    // private BCryptPasswordEncoder passwordEncoder;

    // @Autowired
    // ClientDetailsService clientDetailsService;

   /* @Autowired
    private UserFeign userFeign;*/

    /****
     * 自定义授权认证
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if (StringUtils.isEmpty(username)) {
            return null;
        }
        // 静态指定密码，以后改成通过name到数据库取
        String pwd = new BCryptPasswordEncoder().encode("123");
        // 调用feign
        //Result<com.changgou.user.pojo.User> user = userFeign.findById(username);
        // HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        //创建User对象
        String permissions = "admin";
        //UserJwt userDetails = new UserJwt(username,user.getData().getPassword(),AuthorityUtils.commaSeparatedStringToAuthorityList(permissions));
        UserJwt userDetails = new UserJwt(username, pwd, AuthorityUtils.commaSeparatedStringToAuthorityList(permissions));
        userDetails.setNickname("小鸟");
        userDetails.setId("8789");
        userDetails.setPhone("1320335756");
        return userDetails;
    }
}
