package threadcoreknowledge.stopthreads.volatiledemo;

/**
 * 使用 volatile 关键字来停止线程的错误方式
 */
public class WrongWayVolatile implements Runnable {

    // 使用 volatile 修饰一个布尔类型的成员变量 canceled 作为标记位
    private volatile boolean canceled = false;

    @Override
    public void run() {

        int num = 0;
        try {
            // 下面这段代码用于打印 100 的倍数
            // 并且
            // 这里判断上面设置的标记位（即 成员变量 cancel）是否不为 true
            // 如果不为 true （即表示该线程没有被停止）就执行该 while 循环中的语句
            while (num <= 100000 && !canceled) {
                if (num % 100 == 0) {
                    System.out.println(num + "是100的倍数。");
                }
                num++;
                Thread.sleep(1);
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // 创建当前这个 WrongWayVolatile 类对象 r
        WrongWayVolatile r = new WrongWayVolatile();

        // 创建 Thread 类对象，构造方法中传入当前创建的这个 WrongWayVolatile 类对象 r
        Thread thread = new Thread(r);

        // 调用 Thread 类对象中的 start() 方法启动线程
        thread.start();

        // 让当前 main() 方法所在线程休眠 5 秒
        Thread.sleep(5000);

        // 设置对象 r 中的标记位（即成员变量 canceled）为 true（即表示让当前对象 r 所在线程停止）
        r.canceled = true;


        // 此时
        // 执行上面代码是能够停止当前对象 r 所在线程的
        // 所以
        // 这种使用 volatile 关键字修饰标记位的方式好像是可行的
        // 但是
        // 这种方式的健壮性不够（见 WrongWayVolatileCantStop 包中的代码）

    }
}

