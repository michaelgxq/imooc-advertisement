package threadcoreknowledge.threadobjectclasscommonmethods;

import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 描述：     展示wait和notify的基本用法 1. 研究代码执行顺序 2. 证明wait释放锁
 */
public class Wait {
    // 定义一个 Object 类对象作为成员变量
    public static Object object = new Object();

    // 定义静态内部类 Thread1
    static class Thread1 extends Thread {

        // 注意
        // wait() 方法和 notify() 方法是写在两个不同的同步代码块中的，当前这个同步代码块中只写了 notify() 方法

        @Override
        public void run() {
            // 定义同步代码块，锁对象为上面定义的成员变量 object
            synchronized (object) {
                System.out.println(Thread.currentThread().getName() + "开始执行了");
                try {
                    System.out.println(Thread.currentThread().getName() + "准备进入等待状态");

                    // 进入到该同步代码块的线程调用 Object 对象中的 wait() 方法
                    // 使得此线程陷入等待状态（即 WAITING 状态）（同时该线程所持有的锁对象会被释放，以便让其他线程竞争获取）
                    object.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("线程" + Thread.currentThread().getName() + "获取到了锁。");
            }
        }
    }

    // 定义静态内部类 Thread2
    static class Thread2 extends Thread {

        // 注意
        // wait() 方法和 notify() 方法是写在两个不同的同步代码块中的，当前这个同步代码块中只写了 notify() 方法

        @SneakyThrows
        @Override
        public void run() {
            // 定义同步代码块，锁对象为上面定义的成员变量 object
            synchronized (object) {
                // 进入到该同步代码块的线程调用 Object 对象中的 notify() 方法
                // 使得那些进入等待状态（即 WAITING 状态）的线程中的一个能够被唤醒（即 进入 RUNNABLE 状态）（由 CPU 调度决定唤醒哪一个）
                object.notify();

                // 注意
                // 当前线程在调用了上面的 notify() 方法之后，会把剩余的代码执行完（即执行下面的输出语句）
                // 然后
                // 在退出同步代码块之后再释放它所占用的锁
                // 此时
                // 那个被上面的 notify() 方法唤醒的线程才能获取到锁，并执行它自己同步代码块或同步方法中剩余的代码（即 wait() 方法之后的代码）
                System.out.println("线程" + Thread.currentThread().getName() + "调用了notify()");

                Thread.sleep(2000);
            }
        }
    }


    public static void main(String[] args) throws InterruptedException {

//        Thread1 thread1 = new Thread1();
//        Thread2 thread2 = new Thread2();
//
//        thread1.start();
//
//        Thread.sleep(200);
//
//        thread2.start();
//
//        for (int i = 0; i < 10; i++) {
//            System.out.println("线程 thread0 的状态：" + thread1.getState());
//            System.out.println("线程 thread1 的状态：" + thread2.getState());
//        }


        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("张三", "20");
        Set<Map.Entry<String, String>> entries = stringStringHashMap.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            System.out.println("类型：" + entry.getClass());
        }
    }

    // 上面的代码执行结果为
    // 当 Thread1 类对象所在线程执行了 wait() 方法后，它所占用的同步锁就被释放了，并且该线程陷入了阻塞状态
    // 此时
    // 和 Thread1 类对象所在线程使用同一个锁对象的 Thread2 类对象所在线程就获得了该锁，并开始执行它的 run() 方法中的代码
    // 当它执行了 notify() 方法之后
    // Thread1 类对象所在线程就被唤醒（此时因为就只有两个线程，所以一个调用了 notify() 方法，那么另一个肯定会被唤醒）
    // 因此
    // Thread1 类对象所在线程就会继续执行 object.wait(); 这行代码之后的代码了

}
