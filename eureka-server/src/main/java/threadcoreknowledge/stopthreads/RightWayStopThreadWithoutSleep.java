package threadcoreknowledge.stopthreads;

/**
 *  在 run() 方法中没有 sleep() 或 wait() 方法的情况下停止线程
 */
public class RightWayStopThreadWithoutSleep implements Runnable {

    /**
     * 此 run() 方法中的代码是用于打印 Java 中 Integer 类所能表示的最大整数中所有能被 10000 整除的数
     */
    @Override
    public void run() {
        int num = 0;

        // 通过调用 Thread 类中的 currentThread() 方法来获取当前执行此 run() 方法的线程
        // 然后
        // 再通过此获取到的线程调用 isInterrupted() 方法来判断该线程是否有收到 interrupt（即被要求中断线程）的通知
        // 注意
        // 这里是取反的（即只有在该线程没有收到此 interrupt 通知时才会执行下面的 while 循环）
        while (!Thread.currentThread().isInterrupted() && num <= Integer.MAX_VALUE / 2) {
            if (num % 10000 == 0) {
                System.out.println(num + " 是 10000 的倍数");
            }
            num++;
        }

        System.out.println("任务运行结束了");
    }

    public static void main(String[] args) throws InterruptedException {
        // 创建线程对象 thread
        Thread thread = new Thread(new RightWayStopThreadWithoutSleep());

        // 调用 Thread 类中的 start() 方法来启动线程
        thread.start();

        // 让当前线程（即 main() 方法所在线程）阻塞（即休眠） 2 秒
        // 即
        // 让当前线程阻塞 2 秒后再执行下面的的语句
        Thread.sleep(2000);

        // 调用 Thread 类中的 interrupt() 方法来通知该 Thread 类对象 thread 停止工作
        // 注意
        // 此时，我们是在该 main() 方法所在线程中调用另一个线程的 interrupt() 方法
        // 所以
        // 此时就是使用一个线程去通知另一个线程停止工作
        thread.interrupt();
    }
}
