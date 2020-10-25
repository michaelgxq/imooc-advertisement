package threadcoreknowledge.threadobjectclasscommonmethods;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 描述：     演示sleep不释放lock（lock需要手动释放）
 */
public class SleepDontReleaseLock implements Runnable {

    // 定义 Lock 接口实现类 ReentrantLock 类对象作为成员变量
    private static final Lock lock = new ReentrantLock();

    @Override
    public void run() {
        // 调用 Lock 接口中的 lock() 方法，把当前代码块锁住
        lock.lock();

        System.out.println("线程" + Thread.currentThread().getName() + "获取到了锁");

        try {
            // 通过 Thread 类调用 sleep() 方法，让当前线程休眠
            Thread.sleep(5000);

            System.out.println("线程" + Thread.currentThread().getName() + "已经苏醒");

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 调用 Lock 接口中的 lock() 方法，把当前代码块释放当前代码块的锁
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        // 创建当前类 SleepDontReleaseLock 类的对象
        SleepDontReleaseLock sleepDontReleaseLock = new SleepDontReleaseLock();

        // 分别创建两个线程类对象
        Thread thread1 = new Thread(sleepDontReleaseLock);
        Thread thread2 = new Thread(sleepDontReleaseLock);

        // 分别启动两个线程类对象
        thread1.start();
        thread2.start();
    }

    // 此时
    // 上面代码的执行结果是
    // 虽然 thread1 所在线程在执行了 run() 方法中的 Thread.sleep(5000); 这行代码，进入到休眠状态之后
    // 由于 sleep() 方法不会释放锁
    // 因此
    // thread2 所在线程一直无法获取到锁，从而也就无法执行 run() 方法中的代码
    // 只有当 thread1 所在线程从休眠状态中恢复，并执行完 run() 方法中剩余的代码之后，释放了锁
    // 此时
    // thread2 所在线程才能获取到该锁，并执行 run() 方法中的代码
}
