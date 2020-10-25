package threadcoreknowledge.threadobjectclasscommonmethods;

/**
 * 描述：     演示join期间被中断的效果
 */
public class JoinInterrupt {
    public static void main(String[] args) {
        // 获取当前线程对象（即 main() 方法所在线程）
        Thread mainThread = Thread.currentThread();

        // 创建 Thread 类对象 thread1
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 让 main() 方法所在线程中断
                    mainThread.interrupt();

                    Thread.sleep(5000);

                    System.out.println("Thread1 finished.");

                } catch (InterruptedException e) {
                    System.out.println("子线程中断");
                }
            }
        });

        // 调用 Thread 类中的 start() 方法，启动线程对象 thread1 所在线程
        thread1.start();

        System.out.println("等待子线程运行完毕");

        try {
            // 调用 Thread 类中的 join() 方法，把线程对象 thread1 所在线程加入到当前 main() 方法所在线程中来
            thread1.join();

        } catch (InterruptedException e) {
            // 当对象 thread1 所在线程执行它 run() 方法中的 mainThread.interrupt(); 这行代码之后
            // 当前 main() 方法所在线程就会被中断
            // 此时
            // 就会执行下面这行输出语句
            System.out.println(Thread.currentThread().getName()+"主线程中断了");

            // 如果我们想让那些通过 join() 方法加进来的线程在当前线程被中断之后也被中断
            // 我们就需要在 catch 语句中去手动中断这些线程
            // 中断对象 thread1 所在线程
            thread1.interrupt();
        }

        System.out.println("子线程已运行完毕");
    }

    // 此时
    // 上面的代码执行结果为
    // 如果当前线程（即此时 main() 方法所在线程）被中断之后，那些通过 join() 方法加进来的线程不会受到影响，它们仍会继续执行自己的代码
    // 这也就出现了上面 main() 方法所在线程中的 System.out.println("子线程已运行完毕"); 这行代码执行完毕之后
    // 通过 join() 方法加进来的线程仍会继续执行的情况
    // 因此
    // 如果我们想让那些通过 join() 方法加进来的线程在当前线程被中断之后也被中断
    // 我们就需要在 catch 语句中去手动中断这些线程（即如上面的 thread1.interrupt(); 这行代码所做的那样）
    //（但是我们也知道在调用了 interrupt() 之后，这些线程也不是马上就停止的）

}
