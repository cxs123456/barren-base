package org.barren.core.auth.config;

import org.barren.core.auth.properties.AuthProperties;
import org.barren.core.auth.utils.AuthUtil;
import org.barren.core.auth.utils.UserJwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.builders.ClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author cxs
 **/
@Configuration
@EnableAuthorizationServer
@EnableConfigurationProperties({AuthProperties.class})
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;
    @Autowired
    private AuthProperties properties;
    /***
     * 客户端信息配置，指定客户端登录信息来源
     * 为了测试客户端与凭证存储在内存(生产应该用数据库来存储,oauth有标准数据库模板)
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 使用内存存储2个客户端 app,web
        // clients.inMemory()
        //         // 客户端id  client_id
        //         .withClient("app")
        //         // 客户端秘钥  client_secret
        //         .secret(passwordEncoder().encode("123456"))
        //         // 重定向地址
        //         .redirectUris("http://localhost")
        //         // 访问令牌有效期
        //         .accessTokenValiditySeconds(6000)
        //         // 刷新令牌有效期
        //         .refreshTokenValiditySeconds(9000)
        //         // 该client允许的授权类型 authorization_code, password, refresh_token, implicit, client_credentials
        //         .authorizedGrantTypes("password", "authorization_code", "client_credentials", "refresh_token", "implicit")
        //         // 客户端授权范围，名称自定义，必填
        //         .scopes("all")
        //         .and()
        //         .withClient("web")
        //         .secret(passwordEncoder().encode("123123"))
        //         .authorizedGrantTypes("password")
        //         .scopes("all")
        //         // 自动确认授权
        //         .autoApprove(true);
        ClientDetailsServiceBuilder serviceBuilder = clients.inMemory();
        for (AuthProperties.Client client : properties.getClients()) {
            // 客户端id  client_id
            serviceBuilder.withClient(client.getClientId())
                    // 客户端秘钥  client_secret
                    .secret(passwordEncoder().encode(client.getClientSecret()))
                    // 重定向地址
                    .redirectUris("http://localhost")
                    // 访问令牌有效期
                    .accessTokenValiditySeconds(6000)
                    // 刷新令牌有效期
                    .refreshTokenValiditySeconds(9000)
                    // 该client允许的授权类型 authorization_code, password, refresh_token, implicit, client_credentials
                    .authorizedGrantTypes("password", "authorization_code", "client_credentials", "refresh_token", "implicit")
                    // 客户端授权范围，名称自定义，必填
                    .scopes(client.getScope()).and();
        }

        // 使用数据库
        // clients.jdbc(dataSource).clients(clientDetails());
        // 自定义客户端服务
        // clients.withClientDetails(clientDetailsService())
    }

    // 客户端信息配置
    /*@Bean
    public ClientDetailsService clientDetails() {
        return new JdbcClientDetailsService(dataSource);
    }*/
    /* //从数据库中查询出客户端信息
    @Bean
    public JdbcClientDetailsService clientDetailsService() {
        JdbcClientDetailsService jdbcClientDetailsService = new JdbcClientDetailsService(dataSource);
        jdbcClientDetailsService.setPasswordEncoder(passwordEncoder);
        return jdbcClientDetailsService;
    }*/

    /***
     * 授权服务器的安全配置
     * @param oauthServer
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.allowFormAuthenticationForClients()
                .passwordEncoder(passwordEncoder())
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    /***
     * 授权服务器端点配置，OAuth2的主配置信息
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // 密码模式必须配置，使用spring security 认证管理器
        endpoints.authenticationManager(authenticationConfiguration.getAuthenticationManager());

        // 这里使用客户端jwt token，也可以内存存储token,也可以使用redis和数据库
        endpoints.tokenStore(tokenStore());//令牌存储
        endpoints.tokenServices(tokenServices());
        endpoints.accessTokenConverter(jwtAccessTokenConverter());
        // 密码模式必须配置，用户信息service
        // endpoints.userDetailsService(userDetailsService);
        // endpoints.setClientDetailsService(clientDetailsService);
        // endpoints.userApprovalHandler(userApprovalHandler());
        endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);

    }

    /***
     * 采用BCryptPasswordEncoder对密码进行编码,
     * websecurity用户密码和认证服务器客户端密码都需要加密算法
     * @return
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(this.jwtAccessTokenConverter());
    }

    /**
     * JWT令牌转换器，定义token的生成方式
     *
     * @return
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        // JWT令牌转换器，定义token的生成方式
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        // 对称加密算法 HMAC，设置jwt 密钥，签名和验证使用相同
        converter.setSigningKey(properties.getSecretKey());
        converter.setVerifierKey(properties.getSecretKey());
        //converter.setVerifier(new MacSigner("123456"));

        // 非对称加密算法 RSA，设置jwt密钥对
        // converter.setKeyPair(keyPair);
        return converter;
    }

    /**
     * 该方法用户获取一个token服务对象（该对象描述了token有效期等信息）
     */
    @Bean
    public AuthorizationServerTokenServices tokenServices() {
        // 使用默认实现
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setSupportRefreshToken(true); // 是否开启令牌刷新
        defaultTokenServices.setTokenStore(tokenStore());
        // defaultTokenServices.setClientDetailsService(clientDetailsService);
        // 针对 jwt令牌的添加
        // defaultTokenServices.setTokenEnhancer(jwtAccessTokenConverter());
        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        enhancerChain.setTokenEnhancers(Stream.<TokenEnhancer>of((oAuth2AccessToken, oAuth2Authentication) -> {
            // 在返回 token 中加上一些自定义数据，在jwt中解析也会得到
            UserJwt userJwt = (UserJwt) oAuth2Authentication.getPrincipal();// 获取用户信息
            DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) oAuth2AccessToken;
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("nickname", userJwt.getNickname());
            map.put("id", userJwt.getId());
            map.put("phone", userJwt.getPhone());
            map.put("ok", true);
            token.setAdditionalInformation(map);
            return token;
        }, jwtAccessTokenConverter()).collect(Collectors.toList()));
        defaultTokenServices.setTokenEnhancer(enhancerChain);
        // 设置令牌有效时间（一般设置为2个小时）
        defaultTokenServices.setAccessTokenValiditySeconds(2 * 60 * 60); // access_token就是我们请求资源需要携带的令牌
        // 如果前端觉得麻烦，那么直接设置0或者负数，永远不过期
        // defaultTokenServices.setAccessTokenValiditySeconds(-1);
        // 设置刷新令牌的有效时间
        defaultTokenServices.setRefreshTokenValiditySeconds(259200); // 3天
        return defaultTokenServices;
    }

    @Bean
    public AuthUtil authUtil() {
        AuthUtil authUtil = new AuthUtil(properties.getSecretKey());
        return authUtil;
    }
}
