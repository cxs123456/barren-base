package org.barren.tests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.redisson.RedissonMultiLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * redisson 分布式锁测试
 *
 * @author cxs
 */
@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class RedissonServiceTest {

    @Autowired
    private RedissonClient redissonClient;

    @BeforeEach
    void setUp() throws IOException {

    }


    @Test
    void testMultiLock() throws InterruptedException {
        Thread t0 = new Thread(() -> {
            try {
                multiLock();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Thread t1 = new Thread(() -> {
            try {
                lockObj("lock_a", 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                lockObj("lock_b", 2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread t3 = new Thread(() -> {
            try {
                lockObj("lock_c", 5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t0.start();
        Thread.sleep(1000);
        t1.start();
        t2.start();
        t3.start();

        t0.join();
        t1.join();
        t2.join();
        t3.join();
        System.out.println("exec finish...");
    }

    public void multiLock() throws InterruptedException {
        RLock lockA = redissonClient.getLock("lock_a");
        RLock lockB = redissonClient.getLock("lock_b");
        RLock lockC = redissonClient.getLock("lock_c");
        RedissonMultiLock multiLock = new RedissonMultiLock(lockA, lockB, lockC);
        if (multiLock.tryLock()) {
            try {
                System.out.println("0. lock_a, lock_b and lock_c are being locked........");
                Thread.sleep(3000);
            } finally {
                multiLock.unlock();
                System.out.println("0. lock_a, lock_b and lock_c are unlocked........");
            }
        }
    }

    public void lockObj(String lockKey, long waitTime) throws InterruptedException {
        RLock lock = redissonClient.getLock(lockKey);
        if (lock.tryLock(waitTime, TimeUnit.SECONDS)) {
            try {
                System.out.println("1. get " + lockKey + " ...");
                Thread.sleep(2000);
            } finally {
                lock.unlock();
                System.out.println("1. " + lockKey + " is unlock ...");
            }

        } else {
            System.out.println("1.Can't get " + lockKey + " ...");
        }
    }

    // public void lockB() throws InterruptedException {
    //     RLock lock = redissonClient.getLock("lock_b");
    //     if (lock.tryLock()) {
    //         try {
    //             System.out.println("1.get lock_a...");
    //             Thread.sleep(2000);
    //         } finally {
    //             lock.unlock();
    //         }
    //
    //     } else {
    //         System.out.println("1.Can't get lock_a...");
    //     }
    // }
    //
    // public void lockC() throws InterruptedException {
    //     RLock lock = redissonClient.getLock("lock_c");
    // }

}
