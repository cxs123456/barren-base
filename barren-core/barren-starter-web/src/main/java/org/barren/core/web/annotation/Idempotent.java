package org.barren.core.web.annotation;

import java.lang.annotation.*;

/**
 * 接口幂等注解，用于 controller上，标识作用
 *
 * @author cxs
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Idempotent {
}
