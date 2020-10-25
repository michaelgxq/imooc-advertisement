package threadcoreknowledge.threadobjectclasscommonmethods;

/**
 * 描述：     演示join，注意语句输出顺序，会变化。
 */
public class Join {
    public static void main(String[] args) throws InterruptedException {
        // 创建 Thread 类对象 thread1
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("11111111");

                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "执行完毕");
            }
        });

        // 创建 Thread 类对象 thread2
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("2222222");

                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "执行完毕");
            }
        });

        // 启动上面创建的两个线程
        thread1.start();

        thread2.start();

        System.out.println("hhhh");

        System.out.println("开始等待子线程运行完毕");

        // 分别调用两个线程对象中的 join() 方法
        // 此时
        // 当前线程（即 main() 方法所在线程）会等待这两个线程对象所在线程的 run() 方法执行完毕后
        // 再执行自己剩余的代码（即下面 System.out.println("所有子线程执行完毕"); 这条输出语句）
        thread1.join();
        thread2.join();

        System.out.println("所有子线程执行完毕");
    }
}
