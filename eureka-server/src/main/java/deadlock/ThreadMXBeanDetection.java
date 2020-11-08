package deadlock;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * 用 ThreadMXBean 检测死锁
 * 下面的主体代码和该包下的 TransferMoney 类中的代码基本一致以及和该包下的 MustDeadLock 类中的代码完全一致（用于展示必然死锁）
 * 所以
 * 这里主要就是看和 ThreadMXBean 接口相关的部分
 */
public class ThreadMXBeanDetection implements Runnable {
    // 定义一个标识符 flag
    int flag = 1;

    // 定义两个 Object 类对象，作为锁对象
    static Object o1 = new Object();
    static Object o2 = new Object();

    public static void main(String[] args) throws InterruptedException {
        // 创建两个当前类 ThreadMXBeanDetection 类对象
        ThreadMXBeanDetection r1 = new ThreadMXBeanDetection();
        ThreadMXBeanDetection r2 = new ThreadMXBeanDetection();

        // 分别为两个 ThreadMXBeanDetection 类对象的成员变量 flag 设置不同的值
        //（以便让两个线程分别进入下面 run() 方法中的两个不同的嵌套 synchronized 同步代码块，以形成死锁）
        r1.flag = 1;
        r2.flag = 0;

        // 创建两个 Thread 类对象，构造方法中传入上面创建的 ThreadMXBeanDetection 类对象
        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);

        // 启动上面创建的两个 Thread 类对象
        t1.start();
        t2.start();

        // 让当前线程（即 main() 方法所在线程）休眠 1000 毫秒
        Thread.sleep(1000);

        // 调用 ManagementFactory 类中的 getThreadMXBean() 方法获取 ThreadMXBean 接口的实现类对象
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

        // 调用 ThreadMXBean 接口的实现类中的 findDeadlockedThreads() 方法，获取所有陷入死锁的线程的 ID，存放到数组 deadlockedThreads 中
        long[] deadlockedThreads = threadMXBean.findDeadlockedThreads();

        // 如果上面的数组 deadlockedThreads 不为空，就遍历该数组
        if (deadlockedThreads != null && deadlockedThreads.length > 0) {
            for (int i = 0; i < deadlockedThreads.length; i++) {
                // 调用 ThreadMXBean 接口中的 getThreadInfo() 方法，获取存放那个陷入死锁的线程的相关信息的对象（即 ThreadInfo 类对象）
                //（方法中传入 deadlockedThreads 数组中的对应元素（即线程 ID））
                ThreadInfo threadInfo = threadMXBean.getThreadInfo(deadlockedThreads[i]);

                // 通过上面获取的 ThreadInfo 类对象中的 getThreadName() 方法，获取当前这个陷入死锁的线程的名字，并输出
                System.out.println("发现死锁" + threadInfo.getThreadName());
            }
        }

        // 其实
        // 一般在实际工作中，我们都是在上面的 if 语句中写一些如记录日志 或者 报警机制 或者 重启程序 等操作手段来对发生死锁的线程进行一些处理
    }

    @Override
    public void run() {
        System.out.println("flag = " + flag);
        if (flag == 1) {
            synchronized (o1) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (o2) {
                    System.out.println("线程1成功拿到两把锁");
                }
            }
        }
        if (flag == 0) {
            synchronized (o2) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (o1) {
                    System.out.println("线程2成功拿到两把锁");
                }
            }
        }
    }
}
