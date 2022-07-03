package org.barren.core.web;

import lombok.extern.slf4j.Slf4j;
import org.barren.core.tool.http.R;
import org.barren.core.web.i18n.MessageSourceUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;
import java.util.Objects;

/**
 * 自定义实现spring的全局异常解析器 HandlerExceptionResolver
 *
 * @author cxs
 */
@RestControllerAdvice
@Configuration
@Slf4j
public class GlobalExceptionHandler {


    /**
     * 处理所有不可知的异常
     */
    @ExceptionHandler(Throwable.class)
    public R handleException(Throwable e) {
        // 打印堆栈信息
        log.error("handleException ", e);
        return R.fail(500, MessageSourceUtil.getMessage(500), e.getMessage());
    }

    /**
     * 访问拒绝异常
     *
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(AccessDeniedException.class)
    public R handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("handleAccessDeniedException, requestURI:{}", requestURI, e);
        return R.fail(HttpStatus.UNAUTHORIZED.value(), MessageSourceUtil.getMessage(401), e.getMessage());
    }

    /**
     * 处理所有接口数据验证异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        // 打印堆栈信息
        log.error("handleMethodArgumentNotValidException", e);
        String[] str = Objects.requireNonNull(e.getBindingResult().getAllErrors().get(0).getCodes())[1].split("\\.");
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        String msg = "不能为空";
        if (msg.equals(message)) {
            message = str[1] + ":" + message;
        }
        return R.fail(HttpStatus.BAD_REQUEST.value(), message);
    }
}