package threadcoreknowledge.stopthreads;

/**
 * 最佳实践：catch了InterruptedExcetion之后的优先选择：在方法签名中抛出异常 那么在run()就会强制try/catch
 */
public class RightWayStopThreadInProd implements Runnable {

    @Override
    public void run() {

        while (true && !Thread.currentThread().isInterrupted()) {
            System.out.println("go");

            // 在 run() 方法中通过 try...catch 捕获低层方法抛出的异常
            try {
                throwInMethod();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                //保存日志、停止程序
                System.out.println("保存日志");
                e.printStackTrace();
            }
        }


    }

    /**
     * 在这个被上层方法（即此时的 run() 方法）调用的方法中用 throws 抛出 InterruptedException 异常
     * @throws InterruptedException
     */
    private void throwInMethod() throws InterruptedException {

            Thread.sleep(2000);
            
    }

    public static void main(String[] args) throws InterruptedException {
        // 创建 Thread 类对象，构造方法中传入创建的 Runnable 接口实现类对象
        Thread thread = new Thread(new RightWayStopThreadInProd());

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
