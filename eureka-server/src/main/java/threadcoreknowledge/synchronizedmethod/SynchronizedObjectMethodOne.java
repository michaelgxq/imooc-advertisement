package threadcoreknowledge.synchronizedmethod;

/**
 * @ClassName: SynchronizedObjectMethod
 * @Description: TODO
 * @author: yourName
 * @date: 2020年10月18日 15:26
 */
public class SynchronizedObjectMethodOne implements Runnable {
    // 定义一个数据类型为当前类 SynchronizedObjectMethod 类的成员变量
    static SynchronizedObjectMethodOne instance = new SynchronizedObjectMethodOne();

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
        // 创建两个 Thread 类对象，构造方法中都传入上面定义的成员变量 instance
        Thread thread1 = new Thread(instance);
        Thread thread2 = new Thread(instance);

        // 调用 Thread 类中的 start() 方法来启动上面创建的 2 个线程
        thread1.start();
        thread2.start();

        while (thread1.isAlive() || thread2.isAlive()) {

        }

        System.out.println("finished");
    }

    // 此时
    // 上面代码的执行结果是
    // 对象 thread1 所在线程在执行完 run() 方法后，对象 thread2 所在线程才会去执行 run() 方法
    // 这是因为
    // 由于上面创建的 Thread 类对象 thread1 和 thread2 使用的是同一个 SynchronizedObjectMethodOne 类对象
    // 所以
    // 它们内部的 run() 方法所调用的同步方法 method() 使用的同一个锁对象（即该 SynchronizedObjectMethodOne 类对象）
    // 因此
    // 当对象 thread1 不释放锁对象时，对象 thread2 是无法获取到该锁对象的，从而也就无法执行它所在线程中的同步方法 method()
}
