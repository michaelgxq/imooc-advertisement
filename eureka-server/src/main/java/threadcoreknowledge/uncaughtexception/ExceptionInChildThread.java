package threadcoreknowledge.uncaughtexception;

/**
 * 描述：     单线程，抛出，处理，有异常堆栈 多线程，子线程发生异常，会有什么不同？
 */
public class ExceptionInChildThread implements Runnable {

    @Override
    public void run() {
        // 抛出一个运行时异常
        throw new RuntimeException();
    }

    public static void main(String[] args) {

        try {
            // 创建一个 Thread 类对象并启动
            // 根据上面 run() 方法中的代码
            // 此时这个新建的 Thread 类对象所在的线程会抛出一个运行时异常
            new Thread(new ExceptionInChildThread()).start();
        }
        catch (RuntimeException e) {

            System.out.println("没有打印");
        }

    }

    // 上面的代码执行结果为
    // 虽然我们在主线程（即 main() 方法所在线程）中写了 try...catch 语句想捕获子线程抛出的异常
    // 但是
    // 我们却无法捕获子线程抛出的 RuntimeException 异常
    //（即 catch 语句中的输出语句 System.out.println("没有打印"); 不会被执行）
    // 这就是未捕获异常
}
