package threadcoreknowledge.synchronizedmethod;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName: SynchronizedToLock
 * @Description: TODO
 * @author: yourName
 * @date: 2020年10月18日 22:49
 */
public class SynchronizedToLock {
    // 创建 ReentrantLock 类对象
    Lock lock = new ReentrantLock();

    /**
     * 定义同步方法 method1()
     */
    public synchronized void method1() {
        System.out.println("我是 synchronized 形式的锁");
    }

    /**
     * 定义方法 method2()
     */
    public void method2() {
        // 调用 Lock 类中的 lock() 方法，为当前方法加锁
        lock.lock();

        try {
            System.out.println("我是 Lock 形式的锁");
        }
        finally {
            // 调用 Lock 类中的 unlock() 方法，为当前方法释放锁
            lock.unlock();
        }
    }

    // 注意
    // 上面 method1() 方法和 method2() 方法其实是等价的
    // 即
    // 当线程进入到 method1() 方法时，就相当于执行了 method2() 方法中的 lock.lock(); 这行代码
    // 因为
    // 此时调用这两个线程都会获取锁
    // 当 method1() 方法执行完，就相当于执行了 method2() 方法中的 lock.unlock(); 这行代码
    // 因此
    // 此时调用这两个线程都会释放锁

    public static void main(String[] args) {
        // 创建当前 SynchronizedToLock 类对象
        SynchronizedToLock synchronizedToLock = new SynchronizedToLock();

        // 分别调用 SynchronizedToLock 类中的 method1() 和 method2() 方法
        synchronizedToLock.method1();
        synchronizedToLock.method2();
    }

    // 其实
    // 这个类中的代码就是想在 method2() 方法中通过使用 Lock 接口中的 lock() 和 unlock() 方法来模拟 synchronized 关键字中加锁和释放锁的过程
}
