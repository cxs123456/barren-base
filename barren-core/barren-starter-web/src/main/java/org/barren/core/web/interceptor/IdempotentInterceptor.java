package org.barren.core.web.interceptor;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.barren.core.tool.http.R;
import org.barren.core.web.annotation.Idempotent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 接口幂等拦截器拦截器
 *
 * @author cxs
 */
@Slf4j
@Component
public class IdempotentInterceptor implements HandlerInterceptor {

    public static final String IDEMPOTENT_HEADER = "idempotent-key";

    public static final String IDEMPOTENT_KEY_PREFIX = "IDEMPOTENT-KEY:";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Idempotent idempotent = handlerMethod.getMethodAnnotation(Idempotent.class);
            if (idempotent == null) {
                idempotent = handlerMethod.getMethod().getDeclaringClass().getAnnotation(Idempotent.class);
            }

            if (idempotent != null) {
                //
                String idempotentKey = request.getHeader(IDEMPOTENT_HEADER);
                if (StringUtils.isNotBlank(idempotentKey) && redisTemplate.delete(IDEMPOTENT_KEY_PREFIX + idempotentKey)) {
                }else{
                    log.error("interface idempotent error!");
                    HttpServletResponse servletResponse = (HttpServletResponse) response;
                    servletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    servletResponse.getWriter().write(JSON.toJSONString(R.fail(HttpStatus.METHOD_NOT_ALLOWED.value(), HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase())));
                    return false;
                }
            }
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
