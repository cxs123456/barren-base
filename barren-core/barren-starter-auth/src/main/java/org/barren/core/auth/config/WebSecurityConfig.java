package org.barren.core.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@AutoConfigureAfter(AuthServerConfig.class)
// //开启方法上的PreAuthorize注解
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Order
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 设置用户详情服务
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        //也可以在内存中创建用户并为密码加密
        // auth.inMemoryAuthentication()
        //         .withUser("user").password(passwordEncoder().encode("123")).roles("USER")
        //         .and()
        //         .withUser("admin").password(passwordEncoder().encode("123")).roles("ADMIN");
    }

    /***
     * 设置不拦截资源服务器的认证请求
     * 忽略安全拦截的URL,
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        // web.ignoring().antMatchers(
        //         "/user/login",
        //         "/user/logout");
        // // 默认不拦截/token/**下的路径
        // web.ignoring().antMatchers("/oauth/check_token");
        web.ignoring().antMatchers(HttpMethod.GET,
                "/*.html",
                "/**/*.html",
                "/**/*.css",
                "/**/*.js",
                "/*.ico",
                "/webSocket/**",
                // swagger相关
                "/webjars/**", // Knife4j对OAuth2支持，资源目录：webjars/oauth/oauth2.html
                "/swagger-resources/**","/*/api-docs");
    }

    /****
     * uri权限拦截,生产可以设置为启动动态读取数据库,具体百度
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


    /***
     * 创建授权管理认证对象，oauth2认证服务器需配合spring Security使用
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    // @Override
    // protected void configure(AuthenticationManagerBuilder auth) {
    //     auth.authenticationProvider(customAuthenticationProvider());
    // }


}
