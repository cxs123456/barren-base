package org.barren.core.auth.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

// @Configuration
// @EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    //公钥
    private static final String PUBLIC_KEY = "public.key";

    // /***
    //  * 定义JwtTokenStore
    //  * @param jwtAccessTokenConverter
    //  * @return
    //  */
    // @Bean
    // public TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
    //     return new JwtTokenStore(jwtAccessTokenConverter);
    // }
    //
    // /***
    //  * 定义JJwtAccessTokenConverter
    //  * @return
    //  */
    // @Bean
    // public JwtAccessTokenConverter jwtAccessTokenConverter() {
    //     JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
    //     converter.setVerifierKey(getPubKey());
    //     return converter;
    // }

    /***
     * Http安全配置，对每个到达系统的http请求链接进行校验
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        //所有请求必须认证通过
        http.authorizeRequests()
                //下边的路径放行
                //配置地址放行
                //.antMatchers("/demo/anyone").permitAll()
                .anyRequest()
                .authenticated();    //其他地址需要认证授权
    }


    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
    }


}
