package com.imooc.advertisement;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName: ThreadLocalTest
 * @Description: TODO
 * @author: yourName
 * @date: 2021年12月04日 9:55
 */
@SpringBootTest

@RunWith(SpringRunner.class)
public class ThreadLocalTest {

    @Test
    @Before
    public void test() {
        ThreadLocal<String> stringThreadLocal = new ThreadLocal<>();
        stringThreadLocal.set("123");

        AtomicInteger atomicInteger = new AtomicInteger();
        for (int i = 0; i < 10; i++) {

            atomicInteger.getAndAdd(0x61c88647);
            System.out.println("结果 ==== " + atomicInteger);
        }
        ThreadLocal<String> stringThreadLocal1 = new ThreadLocal<>();
        stringThreadLocal.set("1235");
    }
}
