package threadcoreknowledge.threadobjectclasscommonmethods;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 描述：     每个1秒钟输出当前时间，被中断，观察。
 * Thread.sleep()
 * TimeUnit.SECONDS.sleep()
 */
public class SleepInterrupted implements Runnable{

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new SleepInterrupted());
        thread.start();
        Thread.sleep(6500);
        thread.interrupt();
    }
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println(new Date());
            try {

                // 当我们需要让某个线程休眠一个比较复杂的时间（如下面的 3 小时 25 分 01 秒）
                // 由于
                // Thread 类的 sleep() 方法只接受毫秒单位的时间
                // 所以
                // 对于复杂的时间，我们把它们转为毫秒是很麻烦的
                // 所以
                // 这里我们可以通过 TimeUnit 这个工具类调用 sleep 方法
                // 这样
                // 我们就可以像下面这样连续调用不同时间单位的 sleep() 方法，以达到设置复杂时间的目的
                TimeUnit.HOURS.sleep(3);
                TimeUnit.MINUTES.sleep(25);
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                System.out.println("我被中断了！");
                e.printStackTrace();
            }
        }
    }
}
