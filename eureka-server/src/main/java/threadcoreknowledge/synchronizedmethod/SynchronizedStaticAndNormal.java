package threadcoreknowledge.synchronizedmethod;

/**
 * @ClassName: SynchronizedYesAndNo
 * @Description: 此类用于展示线程同时访问同步方法和非同步方法时，线程的执行情况
 * @author: yourName
 * @date: 2020年10月18日 16:57
 */
public class SynchronizedStaticAndNormal implements Runnable {
    // 定义一个数据类型为当前类 SynchronizedObjectMethod 类的成员变量
    static SynchronizedStaticAndNormal instance = new SynchronizedStaticAndNormal();


    @Override
    public void run() {
        if (Thread.currentThread().getName().equals("Thread-0")) {
            method1();
        }
        else {
            method2();
        }
    }

    /**
     * 定义静态同步方法 method1()
     */
    public static synchronized void method1() {
        System.out.println("使用 synchronized 修饰的静态方法，当前线程为 " + Thread.currentThread().getName());

        try {
            // 让当前线程（即此时那个 method() 方法的线程）休眠 3 秒
            Thread.sleep(3000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(Thread.currentThread().getName() + " 运行结束");
    }


    /**
     * 定义普通同步方法 method2()
     */
    public synchronized void method2() {
        System.out.println("使用 synchronized 修饰的普通同步方法，当前线程为 " + Thread.currentThread().getName());

        try {
            // 让当前线程（即此时那个 method() 方法的线程）休眠 3 秒
            Thread.sleep(3000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(Thread.currentThread().getName() + " 运行结束");
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
    // 上面代码的执行结果为
    // 对象 thread1 所在线程 和 对象 thread2 所在线程会不规则的交替执行各自的同步方法
    // 这是因为
    // 两个线程对象一个调用的是静态同步方法，一个调用的是普通同步方法
    // 因此
    // 两个线程所获取的锁对象是不同的（一个是当前 run() 方法所在类的对象，一个是当前 run() 方法所在类的 .class 文件对象）
    // 所以
    // 不同不同锁对象所控制的方法之间是不会相互影响的

    // 注意
    // 这两个同步方法的执行顺序是不固定的（即完全看 CPU 的调度顺序）
    // 即
    // 有可能对象 thread1 所在线程所调用的 method1() 方法先执行，也有可能对象 thread2 所在线程所调用的 method2() 方法先执行
}
