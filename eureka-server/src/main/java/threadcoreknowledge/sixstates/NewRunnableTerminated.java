package threadcoreknowledge.sixstates;

/**
 * 展示线程的 NEW、RUNNABLE、 TERMINATED 状态
 * 注意
 * 当线程开始执行 run() 方法中的代码，那么该线程就是进入了 RUNNABLE 状态
 * Java 中没有 RUNNING 这个线程状态
 */
public class NewRunnableTerminated implements Runnable {

    public static void main(String[] args) {
        // 创建 Thread 类对象，构造方法中传入当前这个 NewRunnableTerminated 类对象
        Thread thread = new Thread(new NewRunnableTerminated());

        // 调用 Thread 类中的 getState() 方法获取对象 thread 的线程状态，并打印
        //（此时的输出结果肯定是 NEW）
        System.out.println(thread.getState());

        //  调用 Thread 类的 start() 方法启动线程
        thread.start();

        // 调用 Thread 类中的 getState() 方法获取对象 thread 的线程状态，并打印
        //（此时的输出结果肯定是 RUNNABLE，因为当前对象 thread 即使没有获取到 CPU 资源从而执行 run() 方法，那么它的状态也是 RUNNABLE）
        System.out.println(thread.getState());

        try {
            // 让当前线程（即 main() 方法所在线程）阻塞（即休眠） 0.01 秒
            // 即
            // 让当前线程阻塞 0.01 秒后再执行下面的的语句
            Thread.sleep(10);

        } catch (InterruptedException e) {

            e.printStackTrace();
        }

        // 调用 Thread 类中的 getState() 方法获取对象 thread 的线程状态，并打印
        //（此时的输出结果肯定是 RUNNABLE，因为当前对象 thread 所表示的线程正在执行 run() 方法）
        System.out.println(thread.getState());

        try {
            // 让当前线程（即 main() 方法所在线程）阻塞（即休眠） 0.1 秒
            // 即
            // 让当前线程阻塞 0.1 秒后再执行下面的的语句
            Thread.sleep(100);

        } catch (InterruptedException e) {

            e.printStackTrace();
        }

        // 调用 Thread 类中的 getState() 方法获取对象 thread 的线程状态，并打印
        //（此时的输出结果肯定是 TERMINATED，因为此时下面 run() 方法中打印 1000 次的代码早就执行完了）
        System.out.println(thread.getState());
    }

    @Override
    public void run() {
        // 进行 1000 次的打印操作
        for (int i = 0; i < 1000; i++) {
            System.out.println(i);
        }
    }
}
