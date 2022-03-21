package threadcoreknowledge.createthreads;

/**
 * 创建 RunnableStyle 类，该类实现了 Runnable 接口
 */
public class RunnableStyle implements Runnable{

    public static void main(String[] args) {
        Thread thread = new Thread(new RunnableStyle());
        thread.start();
    }

    // 定义线程任务（即 实现 Runnable 接口中的 run() 方法
    @Override
    public void run() {
        System.out.println("用Runnable方法实现线程");
    }
}
