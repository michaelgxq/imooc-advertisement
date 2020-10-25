package threadcoreknowledge.uncaughtexception;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 1.定义一个 UncaughtExceptionHandler 接口的实现类
 */
public class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    private String name;

    public MyUncaughtExceptionHandler(String name) {
        this.name = name;
    }

    /**
     * 实现 UncaughtExceptionHandler 接口中的 uncaughtException() 方法
     * 此方法即用于捕获子线程抛出的异常
     *
     * @param t 此形参用于接收子线程所属的 Thread 类对象
     * @param e 此形参用于接收异常堆栈信息
     */
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        // 创建 Logger 类对象
        Logger logger = Logger.getAnonymousLogger();

        // 使用 Logger 类中的 log() 方法打印线程名和异常堆栈信息
        logger.log(Level.WARNING, "线程异常，终止啦" + t.getName(), e);

        System.out.println(name + "捕获了异常" + t.getName() + "异常");
    }
}
