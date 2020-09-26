package threadcoreknowledge.stopthreads;

/**
 * 如果我们在 while 循环里面放 try...catch，那么会导致中断失效
 */
public class CantInterrupt {

    public static void main(String[] args) throws InterruptedException {
        // 实现 Runnable 接口
        // 此接口中实现的方法是用于获取 100 的整数倍的数
        Runnable runnable = () -> {
            int num = 0;

            // 判断当前 num 是否小于 10000 并且当前线程是否有接收到中断通知
            // 注意
            // 与当前包下其他类中的代码不同点在于
            // 我们这里把 try...catch 语句放入了 while 循环里
            while (num <= 10000 && !Thread.currentThread().isInterrupted()) {

                if (num % 100 == 0) {
                    System.out.println(num + "是100的倍数");
                }

                num++;

                // 在 while 循环中进行 try...catch
                try {

                    // 循环每结束一次就让当前线程阻塞（即休眠）10 毫秒
                    Thread.sleep(10);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        // 创建 Thread 类对象，构造方法中传入上面创建的 Runnable 接口实现类对象
        Thread thread = new Thread(runnable);

        // 调用 Thread 类中的 start() 方法来启动线程
        thread.start();

        // 让当前线程（即 main() 方法所在线程）阻塞（即休眠） 5 秒
        // 即
        // 让当前线程阻塞 5 秒后再执行下面的的语句
        Thread.sleep(5000);

        // 调用 Thread 类中的 interrupt() 方法来发送 interrupt 通知，通知该 Thread 类对象 thread 停止工作
        thread.interrupt();


        // 此时
        // 上面的代码执行结果是
        // 当线程抛出 InterruptedException 异常之后，线程并没有停止，而是会继续执行下去
        // 这是因为
        // 根据 Java 异常处理流程
        // try...catch 语句在捕获完线程之后，会继续执行 try...catch 语句之后的代码
        //（ 即此时会继续执行下一次循环 ）
        // 并且
        // Java 在设计的 sleep() 方法的时候
        // 一旦线程在休眠的时候接收到了 interrupt 通知，那么它在抛出 InterruptedException 异常之后，便会把当前线程的 interrupt 这个标记位给清除
        //（ 即我们此时无法再用 isInterrupted() 判断当前线程是否有接收到中断通知 ）
        // 所以
        // 当该线程执行完 catch 语句中的处理异常的语句，并继续之后的循环时，该线程的 interrupt 这个标记位就不存在了
        // 因此
        // 我们上面代码中 isInterrupted() 方法的判断结果肯定是 false
        // 这样一来
        // 就会产生循环在一直往下走的情况
    }
}
