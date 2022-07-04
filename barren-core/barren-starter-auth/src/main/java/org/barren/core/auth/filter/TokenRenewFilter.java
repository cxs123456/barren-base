package org.barren.core.auth.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.barren.core.auth.properties.AuthProperties;
import org.barren.core.auth.utils.CookieTools;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * jwt token 续期过滤器
 *
 * @author cxs
 */
@Slf4j
public class TokenRenewFilter extends GenericFilterBean {

    private final TokenStore tokenStore;

    private final JwtAccessTokenConverter jwtAccessTokenConverter;
    private final AuthProperties properties;

    public TokenRenewFilter(TokenStore tokenStore, JwtAccessTokenConverter jwtAccessTokenConverter, AuthProperties properties) {
        this.tokenStore = tokenStore;
        this.jwtAccessTokenConverter = jwtAccessTokenConverter;
        this.properties = properties;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String token = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isNotBlank(token) &&
                SecurityContextHolder.getContext().getAuthentication() instanceof OAuth2Authentication) {
            OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
            OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
            String tokenValue = details.getTokenValue();
            // 获取token是否过期
            DefaultOAuth2AccessToken accessToken = (DefaultOAuth2AccessToken) tokenStore.readAccessToken(tokenValue);
            // jwt的过期机制是 payload中 exp 的值是时间戳，exp比较服务器当前时间的大小
            if (!accessToken.isExpired()) {
                // 如果剩余30分钟，就修改token过期时间，重新生成新的token
                int expiresIn = accessToken.getExpiresIn();
                if (expiresIn < properties.getJwtConfig().getDetect()) {
                    // 方式1：使用 刷新token获取 新的 token 和 刷新token，放入cookie中
                    // TODO 还是使用下面方式2方便点
                    // 方式2：取巧，将 token 的payload中exp属性的时间修改更长，再加密生成新的token放入cookie中
                    accessToken.setExpiration(DateUtils.addSeconds(new Date(), properties.getJwtConfig().getRenew()));
                    OAuth2AccessToken enhance = jwtAccessTokenConverter.enhance(accessToken, authentication);
                    String newTokenVal = enhance.getValue();
                    CookieTools.setCookie(httpServletRequest, httpServletResponse, properties.getJwtConfig().getTokenKey(), newTokenVal);
                }
            }
        }
        filterChain.doFilter(request, response);
    }

}
