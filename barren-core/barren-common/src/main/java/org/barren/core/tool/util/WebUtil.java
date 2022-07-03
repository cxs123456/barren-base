package org.barren.core.tool.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.InetSocketAddress;

/**
 * 请求相关工具类
 *
 * @author cxs
 */
public interface WebUtil {

    /**
     * 获取 HttpServletRequest
     *
     * @return
     */
    static HttpServletRequest getHttpServletRequest() {
        HttpServletRequest httpServletRequest = getHttpServletRequestable();
        return httpServletRequest;
    }

    /**
     * 获取 HttpServletRequest
     *
     * @return
     */
    static HttpServletRequest getHttpServletRequestable() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
            return attributes.getRequest();
        }
        return null;
    }

    /**
     * 获取请求的ip地址
     *
     * @return ip地址
     */
    static String getRealIP(ServerHttpRequest request) {
        HttpHeaders httpHeaders = request.getHeaders();
        if (httpHeaders == null) {
            return null;
        }
        String ip = httpHeaders.getFirst("x-forwarded-for");
        String unknown = "unknown";
        if (StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
            ip = httpHeaders.getFirst("Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
            ip = httpHeaders.getFirst("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
            InetSocketAddress inetSocketAddress = request.getRemoteAddress();
            if (inetSocketAddress != null) {
                ip = inetSocketAddress.toString();
            }
        }
        if (StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
            ip = httpHeaders.getFirst("http_client_ip");
        }
        if (StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
            ip = httpHeaders.getFirst("HTTP_X_FORWARDED_FOR");
        }

        // 如果是多级代理，那么取第一个ip为客户ip
        if (ip != null && ip.contains(",")) {
            ip = ip.substring(ip.lastIndexOf(',') + 1, ip.length()).trim();
        }
        return ip;
    }

}