package org.barren.core.web.config;

import lombok.extern.slf4j.Slf4j;
import org.barren.core.web.i18n.MessageSourceUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * i18n 国际化配置
 *
 * @author cxs
 **/
@Configuration
@Slf4j
public class I18nConfig {

    /**
     * locale文件后缀
     */
    public static final String LOCALE_PROPERTIES_SUFFIX = ".properties";
    /**
     * locale文件目录
     */
    public static final String LOCALE_DIR = "i18n/";
    /**
     * locale文件路径
     */
    public static final String LOCALE_PATTERN = "classpath*:/" + LOCALE_DIR + "*messages" + LOCALE_PROPERTIES_SUFFIX;

    /**
     * 读取 错误码国际化文件
     * 不需要配置：
     * 在 配置文件中配置 MessageSource就ok了
     *      spring.messages.basename=classpath:i18n/messages
     *      spring.messages.cache-seconds=3600
     *      spring.messages.encoding=UTF-8
     *
     * @return
     */
    // @Bean
    // public MessageSource messageSource() {
    //     ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    //     Resource[] resources = null;
    //     try {
    //         resources = resolver.getResources(LOCALE_PATTERN);
    //     } catch (IOException e) {
    //         log.error("read i18n file fail", e);
    //     }
    //     ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    //     if (resources != null) {
    //         String[] strResources = new String[resources.length];
    //         for (int i = 0; i < resources.length; i++) {
    //             Resource resource = resources[i];
    //             strResources[i] = LOCALE_DIR
    //                     + resource.getFilename().replace(LOCALE_PROPERTIES_SUFFIX, "");
    //         }
    //         messageSource.setBasenames(strResources);
    //     }
    //     messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
    //     // 缓存7天
    //     // messageSource.setCacheSeconds(604800);
    //     return messageSource;
    // }

    @Bean
    public MessageSourceUtil messageSourceUtils() {
        return new MessageSourceUtil();
    }

}