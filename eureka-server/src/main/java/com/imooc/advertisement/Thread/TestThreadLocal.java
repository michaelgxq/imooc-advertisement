package com.imooc.advertisement.Thread;

/**
 * @ClassName: TestThreadLocal
 * @Description: TODO
 * @author: yourName
 * @date: 2021年12月04日 11:31
 */
public class TestThreadLocal {
    public static void main(String[] args) {
//        ThreadLocal<String> stringThreadLocal = new ThreadLocal<>();
//        stringThreadLocal.set("123");
//
//        AtomicInteger atomicInteger = new AtomicInteger();
//        for (int i = 0; i < 10; i++) {
//
//            atomicInteger.getAndAdd(0x61c88647);
//            System.out.println("结果 ==== " + atomicInteger);
//        }
//        ThreadLocal<String> stringThreadLocal1 = new ThreadLocal<>();
//        stringThreadLocal1.set("1235");

//        User user = new User();
//        user.agg();
//
//        User user1 = new User();
//        user1.agg();

        int hash = 0x61c88647;
        int code = 0x61c88647;

        for (int i = 0; i < 4; i++) {
            code += hash;
            System.out.println("计算出来的值 ===== " + (code & (4 - 1)));
        }
    }
}
