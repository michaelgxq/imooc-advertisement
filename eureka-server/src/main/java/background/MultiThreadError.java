package background;

/**
 * 演示必然死锁
 */
public class MultiThreadError implements Runnable {
    // 定义一个标识符 flag 作为成员变量
    int flag = 1;

    // 创建两个 Object 类对象作为成员变量（它们仅仅是作为锁对象使用，没有其他的作用）
    static Object o1 = new Object();
    static Object o2 = new Object();

    @Override
    public void run() {
        System.out.println("flag = " + flag);

        // 同步代码块 1
        if (flag == 1) {
            synchronized (o1) {
                try {
                    Thread.sleep(500);

                    System.out.println("同步代码块 1 的外层同步代码块");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (o2) {
                    System.out.println("1");
                }
            }
        }

        // 同步代码块 2
        if (flag == 0) {
            synchronized (o2) {
                try {
                    System.out.println("同步代码块 2 的外层同步代码块One");

                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (o1) {
                    System.out.println("0");
                }
            }
        }
    }

    public static void main(String[] args) {
        // 创建两个当前类 MultiThreadError 的对象
        MultiThreadError r1 = new MultiThreadError();
        MultiThreadError r2 = new MultiThreadError();

        // 设置对象 r1 的成员变量 flag 值为 1
        r1.flag = 1;

        // 设置对象 r2 的成员变量 flag 属性值为 0
        r2.flag = 0;

        // 分别启动两个线程，构造方法中传入上面创建的两个对象
        new Thread(r1).start();
        new Thread(r2).start();
    }

    // 上面的代码会形成必然的死锁
    // 即
    // 当程序执行了两次 System.out.println("flag = " + flag); 这条语句（即对象 r1 和 r2 所在线程启动时，各自执行了一次） 之后
    // 程序就卡着不动了（即陷入了死锁）
    // 因为
    // 当线程 A （即对象 r1 所在线程）进入了同步代码块 1 时（这是必然发生的，因为对象 r1 的 flag 成员变量值为 1，它符合同步代码块 1 中的 if 判断语句）
    // 此时
    // 线程 A 获取到了 O1 这个锁对象
    // 与此同时
    // 线程 B （即对象 r2 所在线程）进入了同步代码块 2 时（这是必然发生的，因为对象 r2 的 flag 成员变量值为 0，它符合同步代码块 2 中的 if 判断语句）
    // 线程 B 就获取了 O2 这个锁对象
    // 当线程 A 执行完了 System.out.println("同步代码块 1 的外层同步代码块"); 这条语句后
    // 为了进入它内层的同步代码块而要获取 O2 这个锁对象时
    // 与此同时
    // 线程 B 同样在执行完了 System.out.println("同步代码块 2 的外层同步代码块One"); 这条语句后
    // 为了进入它内层的同步代码块而要获取 O1 这个锁对象时，由于线程 A 正霸占着 O1 锁对象
    // 此时
    // 就形成了死锁
    // 因为
    // 线程 A 和 线程 B 各自都占着自己的锁对象不释放（因为各自的同步代码块没有执行完）
    // 同时又想获取到对方的锁对象（因为都要执行各自同步代码块中内部的同步代码块）
    // 这显然就陷入了死循环

}
