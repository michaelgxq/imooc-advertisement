package threadcoreknowledge.createthreads;

/**
 * 创建 ThreadStyle 类，该类继承 Thread 类
 */
public class ThreadStyle extends Thread{

    // 创建线程任务（即 重写 Thread 类中的 run() 方法）
    @Override
    public void run() {

        System.out.println("用Thread类实现线程");
    }

    public static void main(String[] args) {
        // 启动线程
        new ThreadStyle().start();


        System.out.println("当前核心数：" + Runtime.getRuntime().availableProcessors());
    }
}





