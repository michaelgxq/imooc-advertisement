package threadcoreknowledge.threadobjectclasscommonmethods;

/**
 * 描述：     3个线程，线程1和线程2首先被阻塞，线程3唤醒它们。notify, notifyAll。 start先执行不代表线程先启动。
 */
public class WaitNotifyAll implements Runnable {
    // 定义一个 Object 类对象作为成员变量
    private static final Object resourceA = new Object();



    @Override
    public void run() {
        synchronized (resourceA) {
            System.out.println(Thread.currentThread().getName()+" got resourceA lock.");

            try {
                System.out.println(Thread.currentThread().getName()+" waits to start.");

                // 调用 Object 类中的 wait() 方法，让当前调用此方法的线程陷入阻塞状态
                resourceA.wait();

                System.out.println(Thread.currentThread().getName()+"'s waiting to end.");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) throws InterruptedException {
        // 创建当前类 WaitNotifyAll 类对象
        Runnable r = new WaitNotifyAll();

        // 创建 Thread 类对象 threadA，threadB，构造方法中传入上面创建的 WaitNotifyAll 类对象 r
        Thread threadA = new Thread(r);
        Thread threadB = new Thread(r);

        // 创建 Thread 类对象 threadC，它采用匿名内部类的方式往构造方法中传入 Runnable 接口实现类对象
        Thread threadC = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (resourceA) {
                    // 调用 Object 类中的 notifyAll() 方法，唤醒所有使用此锁对象的线程
                    resourceA.notifyAll();
//                    resourceA.notify();

                    // 注意
                    // 当前线程在调用了上面的 notify() 方法之后，会把剩余的代码执行完（即执行下面的输出语句）
                    // 然后
                    // 在退出同步代码块之后再释放它所占用的锁
                    // 此时
                    // 那个被上面的 notify() 方法唤醒的线程才能获取到锁，并执行它自己同步代码块或同步方法中剩余的代码（即 wait() 方法之后的代码）
                    System.out.println("ThreadC notified.");
                }
            }
        });

        // 分别启动上面 3 个 Thread 类对象
        threadA.start();
        threadB.start();
//        Thread.sleep(200);
        threadC.start();
    }

    // 此时上面代码的执行结果是
    // 虽然对象 threadA 所在线程先启动
    // 但是
    // 当对象 threadA 所在线程和对象 threadB 所在线程都陷入阻塞状态
    // 并且
    // 当 threadC 所在线程执行了 notifyAll() 方法后
    // 先被唤醒的不一定是对象 threadA 所在线程
    // 因此
    // 可见当所有线程都被唤醒之后，哪个线程继续执行完全由 CPU 的调度随机决定
}
