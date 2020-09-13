package threadcoreknowledge.startthread;

/**
 * 描述：     对比start和run两种启动线程的方式
 */
public class StartAndRunMethod {

    public static void main(String[] args) {
        Runnable runnable = () -> {
            // 输出当前线程的名字
            System.out.println(Thread.currentThread().getName());

        };

        // 通过 run() 方法来启动线程
        runnable.run();

        // 通过 start() 方法来启动线程
        new Thread(runnable).start();

        // 此时的输出结果为
        // main
        // Thread-0
        // 显然
        // 上面执行 runnable.run(); 这行语句时，输出的是当前 main() 方法所在的线程的名字
        // 但是
        // 我们需要的是创建一个新的线程
        // 因此
        // 这条语句的执行结果不符合我们的要求
        // 然后
        // 在执行 new Thread(runnable).start(); 这条语句时，输出的是一个新线程的名字
        // 可见
        // 此时这条语句的执行结果是我们所期望的

    }
}
