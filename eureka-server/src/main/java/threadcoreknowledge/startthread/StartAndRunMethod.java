package threadcoreknowledge.startthread;

/**
 * 描述：     对比start和run两种启动线程的方式
 */
public class StartAndRunMethod {

    public static void main(String[] args) {
        Runnable runnable = () -> {
            System.out.println(Thread.currentThread().getName());

        };

        // 通过 run() 方法来启动线程
        runnable.run();

        // 通过 start() 方法来启动线程
        new Thread(runnable).start();
    }
}
