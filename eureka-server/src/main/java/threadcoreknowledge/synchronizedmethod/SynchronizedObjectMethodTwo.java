package threadcoreknowledge.synchronizedmethod;

/**
 * @ClassName: SynchronizedObjectMethod
 * @Description: TODO
 * @author: yourName
 * @date: 2020年10月18日 15:26
 */
public class SynchronizedObjectMethodTwo implements Runnable {
    // 定义两个数据类型为当前类 SynchronizedObjectMethod 类的成员变量
    static SynchronizedObjectMethodTwo instance1 = new SynchronizedObjectMethodTwo();
    static SynchronizedObjectMethodTwo instance2 = new SynchronizedObjectMethodTwo();


    @Override
    public void run() {
        // 调用下面定义的 method() 方法
        method();
    }

    /**
     * 定义同步方法 method()
     */
    public synchronized void method() {
        System.out.println("使用 synchronized 修饰方法，当前线程为 " + Thread.currentThread().getName());

        try {
            // 让当前线程（即此时那个 method() 方法的线程）休眠 3 秒
            Thread.sleep(3000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(Thread.currentThread().getName() + "运行结束");
    }


    public static void main(String[] args) {
        // 创建两个 Thread 类对象，构造方法中都传入上面定义的两个成员变量 instance1 和 instance2
        Thread thread1 = new Thread(instance1);
        Thread thread2 = new Thread(instance2);

        // 调用 Thread 类中的 start() 方法来启动上面创建的 2 个线程
        thread1.start();
        thread2.start();

        while (thread1.isAlive() || thread2.isAlive()) {

        }

        System.out.println("finished");
    }

    // 此时
    // 上面代码的执行结果是
    // 对象 thread1 所在线程 和 对象 thread2 所在线程会不规则的交替执行各自的 run() 方法
    // 这是因为
    // 由于上面创建的 Thread 类对象 thread1 和 thread2 使用的是不同的 SynchronizedObjectMethodOne 类对象
    //（即一个使用的 instance1，另一个使用的是 instance2）
    // 所以
    // 它们内部的 run() 方法所调用的同步方法 method() 使用的是不同的锁对象（即一个使用的 instance1，另一个使用的是 instance2）
    // 因此
    // 这两个线程在执行各自 run() 方法中的同步方法 method() 时不会相互影响
    // 而是根据 CPU 的调度情况执行各自的代码
}
