package threadcoreknowledge.stopthreads;

/**
 * 如果线程在执行过程中，每次循环都会调用 sleep() 方法或 wait() 等方法
 * 那么
 * 我们不需要每次迭代（即每次循环）都调用 isInterrupted() 方法来判断该线程是否有收到 interrupt（即被要求中断线程）的通知
 */
public class RightWayStopThreadWithSleepEveryLoop {
    public static void main(String[] args) throws InterruptedException {
        // 实现 Runnable 接口
        // 此接口中实现的方法是用于获取 100 的整数倍的数
        Runnable runnable = () -> {
            int num = 0;
            try {
                // 判断当前 num 是否小于 10000
                while (num <= 10000) {

                    if (num % 100 == 0) {

                        System.out.println(num + "是100的倍数");
                    }

                    num++;

                    // 循环每结束一次就让当前线程阻塞（即休眠）10 毫秒
                    Thread.sleep(10);
                }

            } catch (InterruptedException e) { // 处理中断异常
                e.printStackTrace();
            }
        };

        // 创建 Thread 类对象，构造方法中传入上面创建的 Runnable 接口实现类对象
        Thread thread = new Thread(runnable);

        // 启动线程
        thread.start();

        // 休眠当前线程（即 main() 方法所在线程）
        // 即
        // 让当前线程阻塞（即休眠） 5 秒后再执行下面的的语句
        Thread.sleep(5000);

        // 调用 Thread 类中的 interrupt() 方法来发送 interrupt 通知，通知该 Thread 类对象 thread 停止工作
        thread.interrupt();

        // 此时
        // 我们执行上面代码的结果就是 5 秒过后，这个新创建的线程就会因抛出 InterruptedException 异常而停止（即中断）
        // 并且
        // 此类中的代码和 RightWayStopThreadWithSleep 类中的代码不同点在于
        // 我们没有在此类的 while 循环判断中设置 Thread.currentThread().isInterrupted() 这段代码来判断当前线程是否有接收到中断通知
        // 但是
        // 这个新创建的线程依然能够正常停止
        // 这是因为
        // 只要线程在休眠的时候接收到中断通知，那么该线程就会抛出 InterruptedException 异常从而停止
        // 由于我们在上面代码的每一次循环中都让这个新线程休眠
        // 那么
        // 当 JVM 执行了上面 thread.interrupt(); 这行语句，发出中断通知之后
        // 如果当前该线程处于休眠状态，那么该线程就会被终止
        // 如果当前该线程没有处于休眠状态，那么该线程会先执行业务逻辑代码，然后在下一次休眠的时候抛出 InterruptedException 异常并停止
        // 所以
        // 此时，Thread.currentThread().isInterrupted() 这段判断是否存在中断通知的代码就是多余的
        // 因此
        // 我们就可以把这段代码去掉

    }
}
