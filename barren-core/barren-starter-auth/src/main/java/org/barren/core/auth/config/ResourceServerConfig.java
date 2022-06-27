package org.barren.core.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
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
        // 此处不要禁止formLogin,code模式测试需要开启表单登陆,
        // 并且/oauth/token不要放开或放入下面ignoring,因为获取token首先需要登陆状态
        http.csrf().disable()
                // 限制基于Request请求访问
                .authorizeRequests()
                // 配置地址放行 oauth/**接口对外开放
                // .antMatchers("/oauth/**").permitAll() 默认oauth2 放行
                // //
                .antMatchers("/auth/captcha").permitAll()
                .antMatchers("/auth/login").permitAll()
                .antMatchers("/demo/anyone").permitAll()
                // 其他请求都需要经过验证
                .anyRequest().authenticated()
                // 启用Http基本身份验证
                .and().httpBasic()
                // 启用表单身份验证
                .and().formLogin();

        // 默认不拦截/token/**下的路径，下面不必配置
        // http.antMatcher("/oauth/**").authorizeRequests().antMatchers("/oauth/**").permitAll().and().csrf().disable();
    }


    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
    }


}
