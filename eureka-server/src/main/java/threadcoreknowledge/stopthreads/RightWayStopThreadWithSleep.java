package threadcoreknowledge.stopthreads;

/**
 * 带有sleep() 方法（即线程阻塞）的中断线程的写法
 */
public class RightWayStopThreadWithSleep {

    public static void main(String[] args) throws InterruptedException {
        // 实现 Runnable 接口
        // 此接口中实现的方法是用于获取 100 的整数倍的数
        Runnable runnable = () -> {
            int num = 0;
            try {
                // 判断当前 num 是否小于 300 并且判断当前线程是否有接收到中断通知
                while (num <= 300 && !Thread.currentThread().isInterrupted()) {
                    if (num % 100 == 0) {
                        System.out.println(num + "是100的倍数");
                    }
                    num++;
                }

                // 当执行完上面的循环之后，让当前线程阻塞（即休眠 1 秒）
                Thread.sleep(1000);

            }
            catch (InterruptedException e) { // 处理中断异常
                e.printStackTrace();
            }
        };

        // 创建 Thread 类对象，构造方法中传入上面创建的 Runnable 接口实现类对象
        Thread thread = new Thread(runnable);

        // 启动线程
        thread.start();

        // 休眠当前线程（即 main() 方法所在线程）
        // 即
        // 让当前线程阻塞（即休眠） 0.5 秒后再执行下面的的语句
        // 注意
        // 由于我们只让当前线程休眠 0.5 秒
        // 而上面我们在 Runnable 接口中实现的方法中，我们让新创建的线程休眠了 1 秒
        // 所以
        // 当此 main() 方法所在线程被唤醒时，新创建的线程还在休眠当中
        Thread.sleep(500);

        // 调用 Thread 类中的 interrupt() 方法来通知该 Thread 类对象 thread 停止工作
        // 注意
        // 此时
        // 由于新创建的线程的休眠实现要比当前 main() 方法所在线程的休眠时间长
        // 所以
        // 当执行此语句时，新创建的线程还在休眠（即阻塞）当中
        // 即
        // 我们是在新线程处于阻塞状态下，停止该新线程的
        thread.interrupt();

        // 当我们执行上面代码的时候
        // 程序是会抛出 InterruptedException 异常（即线程中断异常）的
        // 因为
        // 当一个线程处于休眠（即阻塞）的情况下被中断（即停止）的话，该线程就会抛出该异常
    }
}
