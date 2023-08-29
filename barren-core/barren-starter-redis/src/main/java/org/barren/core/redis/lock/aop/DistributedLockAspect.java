package org.barren.core.redis.lock.aop;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.barren.core.redis.lock.annotation.DistributedLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.expression.Expression;
import org.springframework.expression.TypedValue;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Author: XChen@fintechautomation.com
 * @Created: 2023/3/25
 **/
@Slf4j
@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE) // **分布式锁释放必须事务提交之后**，分布式锁AOP先执行，切面类添加注解 `@Order(1)`，order越小最先执行最后结束。
public class DistributedLockAspect implements BeanFactoryAware {

    private static final SpelExpressionParser parser = new SpelExpressionParser();

    private static final DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    private BeanFactory beanFactory;

    @Autowired
    private RedissonClient redissonClient;

    @Pointcut("@annotation(org.barren.core.redis.lock.annotation.DistributedLock)")
    public void pointCut() {

    }

    @Around(value = "pointCut() && @annotation(distributedLock)")
    public Object around(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Exception {
        // parse method params value
        String bizName = distributedLock.bizName();
        String keyVal = resolveKey(joinPoint, distributedLock.key());
        long waitTime = distributedLock.waitTime();
        TimeUnit unit = distributedLock.unit();
        if (StringUtils.isAnyBlank(bizName, keyVal)) {
            try {
                log.warn("DistributedLock warn, bizName or keyVal is empty, bizName={}, key={}, keyVal = {}", bizName, distributedLock.key(), keyVal);
                // do something...
                return joinPoint.proceed();
            } catch (Throwable e) {
                throw new Exception(e);
            }
        }
        String lockKey = bizName + keyVal;
        RLock lock = redissonClient.getLock(lockKey);
        if (lock.tryLock(waitTime, unit)) {
            try {
                // do something...
                return joinPoint.proceed();
            } catch (Throwable e) {
                throw new Exception(e);
            } finally {
                lock.unlock();
            }
        } else {
            throw new RuntimeException("System business resources have not been released, lockKey=" + lockKey);
        }
    }

    private String resolveKey(ProceedingJoinPoint joinPoint, String key) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Expression expression = parser.parseExpression(key);
        StandardEvaluationContext context = new MethodBasedEvaluationContext(TypedValue.NULL, method,
                joinPoint.getArgs(), nameDiscoverer);
        // it only parses method params '#param-name', No need to parse been '@been-name'
        // context.setBeanResolver(new BeanFactoryResolver(beanFactory));
        Object value = expression.getValue(context);
        return Objects.toString(value, "");
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
