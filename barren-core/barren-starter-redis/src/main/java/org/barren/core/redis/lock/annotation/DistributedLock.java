package org.barren.core.redis.lock.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author: XChen@fintechautomation.com
 * @Created: 2023/3/25
 **/
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface DistributedLock {

    /**
     * redisson lock key use Spring-EL expression to parse method params value. <br/>
     * e.g. #user.id
     *
     * @return the Spring-EL expression to be evaluated before invoking the protected
     */
    @AliasFor("value")
    String key() default "";;

    @AliasFor("key")
    String value() default "";;

    /**
     * bizName Specifies the key prefix
     * @return
     */
    String bizName() ;

    long waitTime() default 30;

    TimeUnit unit() default TimeUnit.SECONDS;
}
