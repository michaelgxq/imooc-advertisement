package threadcoreknowledge.stopthreads;

/**
 * 描述：
 *      最佳实践2：
 *      如果我们不想或者无法让底层方法向上抛出 InterruptedException 异常
 *      那么
 *      为了能让上层方法能得知当前线程接收到了中断通知
 *      那么
 *      我们就需要在底层方法用于捕获 InterruptedException 异常的 catch 子语句中调用 Thread.currentThread().interrupt() 来恢复设置中断状态
 *      以便于在后续的代码执行中，上层方法依然能够检查到刚才发生了中断
 */
public class RightWayStopThreadInProd2 implements Runnable {

    @Override
    public void run() {
        while (true) {
            // 在当前这个循环的中使用 if 语句判断当前这个线程是否接受到中断通知
            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Interrupted，程序运行结束");

                break;
            }

            // 调用下面定义的 reInterrupt() 方法
            reInterrupt();
        }
    }


    /**
     * 该 reInterrupt() 方法是一个低层方法（因为它要被上面的 run() 方法调用）
     */
    private void reInterrupt() {
        try {
            Thread.sleep(2000);
        }
        catch (InterruptedException e) {
            // 一旦线程在处于阻塞的状态下接受到了中断通知，并且抛出异常，而且被我们捕获了
            // 那么
            // 当前线程的中断标记位就会被 JVM 清除，从而导致上层方法（即此时的 run() 方法）无法通过 isInterrupted() 方法来判断当前线程是否有收到中断通知
            // 所以
            // 我们这里必须再手动调用一次 interrupt() 方法来让 JVM 发出中断通知
            // 以便上层方法能通过 isInterrupted() 方法来判断当前线程是否有收到中断通知
            Thread.currentThread().interrupt();

            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // 创建 Thread 类对象，构造方法中传入创建的 Runnable 接口实现类对象
        Thread thread = new Thread(new RightWayStopThreadInProd2());

        // 调用 Thread 类中的 start() 方法来启动线程
        thread.start();

        // 让当前线程（即 main() 方法所在线程）阻塞（即休眠） 1 秒
        // 即
        // 让当前线程阻塞 1 秒后再执行下面的的语句
        Thread.sleep(1000);

        // 调用 Thread 类中的 interrupt() 方法来发送 interrupt 通知，通知该 Thread 类对象 thread 停止工作
        thread.interrupt();
    }
}
