package jmm;

import java.util.concurrent.CountDownLatch;

/**
 * 描述：     演示重排序的现象 “直到达到某个条件才停止”，测试小概率事件
 */
public class OutOfOrderExecution {

    // 定义四个 int 类型的成员变量
    private static int x = 0, y = 0;
    private static int a = 0, b = 0;

    public static void main(String[] args) throws InterruptedException {
        // 定义变量 i 用于计数
        int i = 0;

        // 定义一个死循环
        for (; ; ) {
            i++;

            x = 0;
            y = 0;
            a = 0;
            b = 0;

            // CountDownLatch 类是一个并发工具类，我们这里可以先不用去管
            CountDownLatch latch = new CountDownLatch(3);

            // 创建线程对象 one
            Thread one = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        latch.countDown();
                        latch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    a = 1;
                    x = b;
                }
            });

            // 创建线程对象 two
            Thread two = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        latch.countDown();
                        latch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    b = 1;
                    y = a;
                }
            });

            // 启动线程对象 one 和 two
            two.start();
            one.start();

            latch.countDown();

            // 调用 Thread 类的 join() 方法，等对象 one 和 two 所在线程都执行完后，在执行 main() 方法中下面的代码
            one.join();
            two.join();

            String result = "第" + i + "次（" + x + "," + y + ")";


            if (x == 0 && y == 0) {
                System.out.println(result);

                // 如果满足该 if 判断就退出此死循环
                break;
            }
            else {
                System.out.println(result);
            }
        }
    }

    // 从上面的代码我们可以看出
    // 我们给 x, y, a, b 这四个变量写的赋值语句的顺序是
    // 在线程对象 one 中是
    //    a = 1;
    //    x = b;
    // 在线程对象 two 中是
    //    b = 1;
    //    y = a;
    // 但是
    // 由于重排序的存在
    // CPU 在执行上面的代码时，可能不是按照我们在每个线程对象中写的顺序执行的
    // 即
    // CPU 有可能在执行线程对象 two 中的这两行代码时的真正顺序为
    //    y = a;
    //    b = 1;
    // 这就是重排序

    // 加上我们这里是有两个线程（即线程对象 one 所在线程和线程对象 two 所在线程）在交替执行
    // 所以
    // CPU 可能会存在以下执行上面 4 种赋值语句的顺序
    // 即
    //    y = a;
    //    a = 1;
    //    x = b;
    //    b = 1;
    // 此时
    // 就会出现罕见的 x 的值为 0，y 的值为 0 的情况
    // 而这种情况就是重排序造成的

    // 如果
    // 我们给变量 x，y，a，b 加上 volatile 关键字，那么就能通过 volatile 关键字禁用指令重排序的功能，避免上面这种情况的发生
}
