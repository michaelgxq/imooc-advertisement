package threadcoreknowledge.synchronizedmethod;

/**
 * @ClassName: SynchronizedYesAndNo
 * @Description: 此类用于展示线程同时访问同步方法和非同步方法时，线程的执行情况
 * @author: yourName
 * @date: 2020年10月18日 16:57
 */
public class SynchronizedDifferentMethod implements Runnable {
    // 定义一个数据类型为当前类 SynchronizedObjectMethod 类的成员变量
    static SynchronizedDifferentMethod instance = new SynchronizedDifferentMethod();


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
     * 定义同步方法 method1()
     */
    public synchronized void method1() {
        System.out.println("使用 synchronized 修饰方法一，当前线程为 " + Thread.currentThread().getName());

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
     * 定义同步方法 method2()
     */
    public synchronized void method2() {
        System.out.println("使用 synchronized 修饰的同步方法二，当前线程为 " + Thread.currentThread().getName());

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
    // 只有当一个线程所调用的 methodX() 方法执行完毕时，另一个线程才能执行它所对应的 methodX() 方法
    // 这是因为
    // 由于两个线程对象在创建时，构造方法中传入的是同一个类对象（即此时的 instance）
    // 所以
    // 此时两个线程使用的是同一个锁对象
    // 因此
    // 只有当一个线程在执行完它对应的同步方法，然后释放锁对象之后，另一个对象才能获取到对应的锁，再执行它所对应的同步方法

    // 注意
    // 这两个同步方法的执行顺序是不固定的（即完全看 CPU 的调度顺序）
    // 即
    // 有可能对象 thread1 所在线程所调用的 method1() 方法先执行，也有可能对象 thread2 所在线程所调用的 method2() 方法先执行
}
