package threadcoreknowledge.uncaughtexception;

/**
 * 描述：     使用刚才自己写的 UncaughtExceptionHandler
 */
public class UseOwnUncaughtExceptionHandler implements Runnable {

    @Override
    public void run() {
        throw new RuntimeException();
    }

    public static void main(String[] args) throws InterruptedException {
        // 在主线程中使用 Thread 类中的 setDefaultUncaughtExceptionHandler() 方法
        // 该方法中传入 UncaughtExceptionHandler 接口实现类对象（并且在调用构造方法时，我们可以设置该实现类对象的名字（即此时的 捕获器1））
        Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler("捕获器1"));

        // 创建并启动子线程
        new Thread(new UseOwnUncaughtExceptionHandler(), "MyThread-1").start();
        Thread.sleep(300);
        new Thread(new UseOwnUncaughtExceptionHandler(), "MyThread-2").start();
        Thread.sleep(300);
        new Thread(new UseOwnUncaughtExceptionHandler(), "MyThread-3").start();
        Thread.sleep(300);
        new Thread(new UseOwnUncaughtExceptionHandler(), "MyThread-4").start();

        // 此时
        // 一旦子线程抛出了异常，就会被我们创建的 UncaughtExceptionHandler 接口实现类对象中我们实现的 uncaughtException() 方法所捕获
        // 并且执行该方法中的代码（即此时我们写的打印堆栈信息等代码）
    }
}
