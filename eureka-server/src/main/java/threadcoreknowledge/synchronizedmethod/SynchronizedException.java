package threadcoreknowledge.synchronizedmethod;

/**
 * @ClassName: SynchronizedYesAndNo
 * @Description: 此类用于展示线程同时访问同步方法和非同步方法时，线程的执行情况
 * @author: yourName
 * @date: 2020年10月18日 16:57
 */
public class SynchronizedException implements Runnable {
    // 定义一个数据类型为当前类 SynchronizedObjectMethod 类的成员变量
    static SynchronizedException instance = new SynchronizedException();


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
        System.out.println("使用 synchronized 修饰的方法一，当前线程为 " + Thread.currentThread().getName());

        try {
            // 让当前线程（即此时那个 method() 方法的线程）休眠 3 秒
            Thread.sleep(3000);

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // 抛出运行时异常
        throw new RuntimeException();
    }


    /**
     * 定义普通同步方法 method2()
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
    // 当对象 thread1 所在线程调用的 method1() 方法抛出异常之后，对象 thread2 所在线程会继续执行它所调用的 method2() 方法
    // 这是因为
    // 一旦某个同步方法抛出异常
    // 那么
    // 该同步方法所占用的锁会被 JVM 给释放
    // 因此
    // 其他线程就能获取到这把锁，从而执行该线程所要执行的同步方法

}
