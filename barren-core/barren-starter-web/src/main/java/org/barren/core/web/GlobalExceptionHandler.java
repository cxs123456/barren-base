package org.barren.core.web;

import lombok.extern.slf4j.Slf4j;
import org.barren.core.tool.http.R;
import org.barren.core.web.i18n.MessageSourceUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

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
     * 访问拒绝异常，一般指的是方法接口未授权 {@link Authentication#getAuthorities()}
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
     * 处理所有接口数据验证异常，比如{@link Valid} 和 {@link Validated} 效验的参数异常。
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        // 打印错误日志
        log.error("handleMethodArgumentNotValidException", e);
        StringBuilder msg = new StringBuilder();
        BindingResult result = e.getBindingResult();
        List<ObjectError> errors = result.getAllErrors();
        errors.forEach(err -> {
            FieldError fieldError = (FieldError) err;
            log.warn("Bad Request Parameters: dto entity [{}],field [{}],message [{}]", fieldError.getObjectName(), fieldError.getField(), fieldError.getDefaultMessage());
            msg.append(fieldError.getDefaultMessage());
        });
        return R.fail(HttpStatus.BAD_REQUEST.value(), msg.toString());
    }
}