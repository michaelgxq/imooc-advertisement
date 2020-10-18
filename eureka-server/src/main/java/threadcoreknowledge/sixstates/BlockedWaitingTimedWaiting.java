package threadcoreknowledge.sixstates;

/**
 * 展示线程的 BLOCKED, WAITING, TIMED_WAITING 状态
 */
public class BlockedWaitingTimedWaiting implements Runnable{
    public static void main(String[] args) {
        // 创建当前 BlockedWaitingTimedWaiting 类对象
        BlockedWaitingTimedWaiting runnable = new BlockedWaitingTimedWaiting();

        // 创建 Thread 类对象，构造方法中传入上面创建的 BlockedWaitingTimedWaiting 类对象 runnable
        Thread thread1 = new Thread(runnable);

        // 调用 Thread 类中的 start() 方法启动线程
        thread1.start();

        // 创建 Thread 类对象，构造方法中传入上面创建的 BlockedWaitingTimedWaiting 类对象 runnable
        Thread thread2 = new Thread(runnable);

        // 调用 Thread 类中的 start() 方法启动线程
        thread2.start();


        try {
            // 让当前线程（即 main() 方法所在线程）阻塞（即休眠） 0.05 秒
            // 即
            // 让当前线程阻塞 0.05 秒后再执行下面的的语句
            Thread.sleep(5);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 调用 Thread 类中的 getState() 方法获取对象 thread1 的线程状态，并打印
        //（此时的输出结果为 Timed_Waiting 状态，因为此时该线程正在执行 Thread.sleep(1000); 这行代码）
        System.out.println(thread1.getState());

        // 调用 Thread 类中的 getState() 方法获取对象 thread2 的线程状态，并打印
        //（此时的输出结果为 BLOCKED 状态，因为对象 thread2 此时无法想获取由 thread1 占有的同步锁，因为此时对象 thread1 正在执行 syn() 这个同步方法）
        System.out.println(thread2.getState());
        try {
            // 让当前线程（即 main() 方法所在线程）阻塞（即休眠） 1.3 秒
            // 即
            // 让当前线程阻塞 1.3 秒后再执行下面的的语句
            Thread.sleep(1300);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 调用 Thread 类中的 getState() 方法获取对象 thread1 的线程状态，并打印
        //（此时的输出结果为 WAITING 状态，因为我们在下面的 syn() 方法中调用了 wait() 方法，所以当对象 thread1 在执行到 wait() 方法时，它就会进入到 WAITING 状态）
        System.out.println(thread1.getState());

    }

    @Override
    public void run() {
        syn();
    }

    /**
     * 定义同步方法 syn()
     */
    private synchronized void syn() {
        try {
            // 让当前线程（即 Thread 对象所在线程）阻塞（即休眠） 1 秒
            // 即
            // 让当前线程阻塞 1 秒后再执行下面的的语句
            // 这样做的目的是为了不让该 syn() 方法中的代码太快的执行完
            // 因为
            // 一旦改方法执行完成，那么它所占有的同步锁就会被释放
            // 所以
            // 为了不让同步锁被过快的释放，就需要让当前线程休眠 1 秒
            Thread.sleep(1000);


            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
