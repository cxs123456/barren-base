package org.barren.modules.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.barren.core.redis.lock.annotation.DistributedLock;
import org.barren.modules.system.entity.SysTenant;
import org.barren.modules.system.entity.SysUser;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;

/**
 * @Author: XChen@fintechautomation.com
 * @Created: 2023/3/25
 **/
@Slf4j
@Service
public class DemoService {


    @DistributedLock(key = "#lockKey", bizName = "Account_")
    public void lockInfo(String lockKey) throws InterruptedException {
        String currentThreadName = Thread.currentThread().getName();
        System.out.println(" ThreadName=" + currentThreadName + " get lock " + lockKey + " ...");
        Thread.sleep(2000);


    }

    @DistributedLock(key = "#user.id", bizName = "SysUser_")
    public void lockUser(SysUser user) throws InterruptedException {
        Long userId = user.getId();
        String currentThreadName = Thread.currentThread().getName();
        System.out.println(" ThreadName=" + currentThreadName + " get userId =" + userId + " ...");
        Thread.sleep(2000);
        System.out.println(" ThreadName=" + currentThreadName + " release userId =" + userId + " ...");

    }

    @DistributedLock(key = "#tenant.id", bizName = "SysTenant_")
    public void lockTenant(SysTenant tenant) throws InterruptedException {
        Long tenantId = tenant.getId();
        String currentThreadName = Thread.currentThread().getName();
        System.out.println(" ThreadName=" + currentThreadName + " get tenantId =" + tenantId + " ...");
        Thread.sleep(2000);
        System.out.println(" ThreadName=" + currentThreadName + " release tenantId =" + tenantId + " ...");
    }

    /**
     * 测试 {@link AopContext#currentProxy()} 获取当前类的代理对象
     * @throws InterruptedException
     */
    public void testAopContext() throws InterruptedException {
        DemoService service = (DemoService) AopContext.currentProxy();
        service.lockInfo("abc");
    }
}
