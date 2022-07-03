package org.barren.core.web.i18n;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.text.MessageFormat;
import java.util.Locale;

/**
 * 国际化消息处理类
 *
 * @author cxs
 **/
@Slf4j
public class MessageSourceUtil {

    public static MessageSource MESSAGES = null;

    @Autowired
    public void messageSource(MessageSource messageSource) {
        MESSAGES = messageSource;
    }

    public static String getMessage(Integer key, Object... params) {
        return getMessage(String.valueOf(key), params);
    }

    /**
     * 获取国际化信息
     *
     * @param key    错误码
     * @param params 格式化消息参数
     * @return
     */
    public static String getMessage(String key, Object... params) {
        try {
            // 获取语言，这个语言是从header中的Accept-Language中获取的，
            // 会根据Accept-Language的值生成符合规则的locale，如zh、pt、en等
            Locale locale = LocaleContextHolder.getLocale();
            String message = MESSAGES.getMessage(key, null, locale);
            // 使用hutool处理占位符 {}，国际化 MessageFormat处理占位符是{0},{1}
            if (StringUtils.isNotBlank(message) && ArrayUtils.isNotEmpty(params)) {
                return StrUtil.format(message, params);
            }
            return message;
        } catch (Exception e) {
            log.error(" MessageSource error", e);
            return null;
        }
    }

    public static void main(String[] args) {
        // 1.StrUtil.format()
        System.out.println(StrUtil.format("参数校验错误{}:{}", null, null));
        // 2.MessageFormat.format()
        System.out.println(MessageFormat.format("请求参数{0}不能为空", 1));
    }

}